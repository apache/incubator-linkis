/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.entrance.cli.heartbeat

import org.apache.commons.lang3.concurrent.BasicThreadFactory
import org.apache.linkis.common.utils.{Logging, Utils}
import org.apache.linkis.entrance.conf.EntranceConfiguration
import org.apache.linkis.entrance.exception.{EntranceErrorCode, EntranceErrorException}
import org.apache.linkis.entrance.execute.EntranceJob
import org.apache.linkis.scheduler.queue.Job

import java.util
import java.util.concurrent.{ConcurrentHashMap, ScheduledThreadPoolExecutor, TimeUnit}
import scala.collection.JavaConverters._

class CliHeartbeatMonitor(handler: HeartbeatLossHandler) extends Logging {
  private val infoMap = new ConcurrentHashMap[String, EntranceJob]
  private val clientHeartbeatThreshold = 1000 * EntranceConfiguration.CLI_HEARTBEAT_THRESHOLD_SECONDS
  private val clientHeartbeatDaemon = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("entrance-cli-heartbeat-%d").daemon(true).build)

  def panicIfNull(obj: Any, msg: String): Unit = {
    if (obj == null) {
      throw new EntranceErrorException(EntranceErrorCode.VARIABLE_NULL_EXCEPTION.getErrCode, msg)
    }
  }

  /*
  register for scan
   */
  def registerIfCliJob(job: Job): Unit = {
    job match {
      case entranceJob: EntranceJob =>
        if (isCliJob(entranceJob)) {
          val id = entranceJob.getJobRequest.getId.toString
          if (infoMap.containsKey(id)) {
            error("registered duplicate job!! job-id: " + id)
          } else {
            infoMap.put(id, entranceJob)
            info("registered cli job: " + id)
          }
        }
      case _ =>
    }
  }

  /*
  remove from scan list
 */
  def unRegisterIfCliJob(job: Job): Unit = {
    job match {
      case entranceJob: EntranceJob =>
        if (isCliJob(entranceJob)) {
          val id = entranceJob.getJobRequest.getId.toString
          infoMap.remove(id)
          info("unregistered cli job: " + id)
        }
      case _ =>
    }
  }

  /*
  probably will not use it but instead update heartbeat for all jobs and
  scan only cli jobs
   */
  def updateHeartbeatIfCliJob(job: Job): Unit = {
    job match {
      case entranceJob: EntranceJob =>
        if (isCliJob(entranceJob)) {
          val id = entranceJob.getJobRequest.getId.toString
          if (!infoMap.containsKey(id)) error("heartbeat on non-existing job!! job-id: " + id)
          else infoMap.get(id).updateNewestAccessByClientTimestamp()
        }
      case _ =>
    }
  }

  def start(): Unit = {
    panicIfNull(handler, "handler should not be null")
    clientHeartbeatDaemon.scheduleAtFixedRate(new Runnable {
      override def run(): Unit = Utils.tryCatch(scanOneIteration()) {
        t => error("ClientHeartbeatMonitor failed to scan for one iteration", t)
      }
    }, 0, 5, TimeUnit.SECONDS)
    info("started cliHeartbeatMonitor")
    Utils.addShutdownHook(() -> this.shutdown())
  }

  def scanOneIteration(): Unit = { //        LOG.info("ClientHeartbeatMonitor starts scanning for one iteration");
    val currentTime = System.currentTimeMillis
    val entries = infoMap.entrySet.iterator
    val problemJobs = new util.ArrayList[EntranceJob]
    while (entries.hasNext) {
      val entry = entries.next
      debug("Scanned job: " + entry.getKey());
      if (!isAlive(currentTime, entry.getValue)) {
        info("Found linkis-cli connection lost job: " + entry.getKey())
        problemJobs.add(entry.getValue)
      }
    }

    val iterator = problemJobs.iterator
    while (iterator.hasNext) {
      //remove to avoid handle same job twice
      infoMap.remove(iterator.next)
    }

    if (problemJobs.size > 0) {
      handler.handle(problemJobs.asScala.toList)
    }
    debug("ClientHeartbeatMonitor ends scanning for one iteration")
  }

  private val monitorCreators = EntranceConfiguration.CLIENT_MONITOR_CREATOR.getValue.split(",")

  private def isCliJob(job: EntranceJob): Boolean = {
    monitorCreators.exists(job.getCreator.equalsIgnoreCase)
  }

  private def isAlive(currentTime: Long, job: EntranceJob): Boolean = {
    val lastAliveTime = job.getNewestAccessByClientTimestamp
    (currentTime - lastAliveTime) <= clientHeartbeatThreshold
  }

  def shutdown(): Unit = {
    clientHeartbeatDaemon.shutdownNow
  }
}
