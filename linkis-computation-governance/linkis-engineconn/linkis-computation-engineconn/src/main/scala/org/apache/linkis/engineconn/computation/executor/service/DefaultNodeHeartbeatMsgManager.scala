/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.engineconn.computation.executor.service

import org.apache.linkis.common.utils.{Logging, Utils}
import org.apache.linkis.engineconn.acessible.executor.info.NodeHeartbeatMsgManager
import org.apache.linkis.engineconn.computation.executor.metrics.ComputationEngineConnMetrics
import org.apache.linkis.engineconn.core.EngineConnObject
import org.apache.linkis.engineconn.executor.entity.{Executor, SensibleExecutor}
import org.apache.linkis.governance.common.constant.ec.ECConstants
import org.apache.linkis.server.BDPJettyServerHelper

import org.springframework.stereotype.Component

import java.util

import scala.collection.JavaConverters.mapAsScalaMapConverter

@Component
class DefaultNodeHeartbeatMsgManager extends NodeHeartbeatMsgManager with Logging {

  override def getHeartBeatMsg(executor: Executor): String = {
    // 是否手动释放、空闲资源空闲至主动释放时长； 新增引擎非占用空闲总时长、占用时长、占用但空闲总时长、占用且忙碌时长
    val msgMap = new util.HashMap[String, Object]()
    msgMap.put(ECConstants.EC_TICKET_ID_KEY, EngineConnObject.getEngineCreationContext.getTicketId)
    msgMap.put(
      ECConstants.EC_UNLOCK_TO_SHUTDOWN_TIME_MILLS_KEY,
      ComputationEngineConnMetrics.getUnlockToShutdownDurationMills()
    )
    executor match {
      case sensibleExecutor: SensibleExecutor =>
        val status = sensibleExecutor.getStatus
        msgMap.put(
          ECConstants.EC_TOTAL_UNLOCK_TIME_MILLS_KEY,
          ComputationEngineConnMetrics.getTotalUnLockTimeMills(status)
        )
        msgMap.put(
          ECConstants.EC_TOTAL_IDLE_TIME_MILLS_KEY,
          ComputationEngineConnMetrics.getTotalIdleTimeMills(status)
        )
        msgMap.put(
          ECConstants.EC_TOTAL_BUSY_TIME_MILLS_KEY,
          ComputationEngineConnMetrics.getTotalBusyTimeMills(status)
        )
        msgMap.put(
          ECConstants.EC_TOTAL_LOCK_TIME_MILLS_KEY,
          ComputationEngineConnMetrics.getTotalLockTimeMills(status)
        )
      case _ =>
    }
    Utils.tryCatch(BDPJettyServerHelper.gson.toJson(msgMap)) { case e: Exception =>
      val msgs = msgMap.asScala
        .map { case (k, v) => if (null == v) s"${k}->null" else s"${k}->${v.toString}" }
        .mkString(",")
      val errMsg = e.getMessage
      logger.error(s"Convert msgMap to json failed because : ${errMsg}, msgMap values : {${msgs}}")
      "{\"errorMsg\":\"Convert msgMap to json failed because : " + errMsg + "\"}"
    }
  }

}
