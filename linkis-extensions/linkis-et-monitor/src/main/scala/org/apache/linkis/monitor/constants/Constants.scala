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

package org.apache.linkis.monitor.constants

import org.apache.linkis.common.conf.CommonVars

object Constants {

  val SCAN_PREFIX_ERRORCODE = "jobhistory.errorcode."
  val SCAN_PREFIX_UNFINISHED_JOBTIME_EXCEED_SEC = "jobhistory.unfinished.time.exceed.sec."
  val ALERT_RESOURCE_MONITOR = "ecm.resource.monitor.im."

  val UNFINISHED_JOB_STATUS =
    "Inited,WaitForRetry,Scheduled,Running".split(",").map(s => s.toUpperCase())

  val FINISHED_JOB_STATUS =
    "Succeed,Failed,Cancelled,Timeout".split(",").map(s => s.toUpperCase())

  val DATA_FINISHED_JOB_STATUS_ARRAY = "Succeed,Failed,Cancelled,Timeout".split(",")

  val DATA_UNFINISHED_JOB_STATUS_ARRAY =
    "Inited,WaitForRetry,Scheduled,Running".split(",")

  val ALERT_PROPS_FILE_PATH = CommonVars.properties.getProperty(
    "linkis.alert.conf.file.path",
    "linkis-et-monitor-file.properties"
  )

  val ALERT_IMS_URL = CommonVars.properties.getProperty(
    "linkis.alert.url",
    "http://127.0.0.1:10812/ims_data_access/send_alarm.do"
  )

  val ALERT_SUB_SYSTEM_ID =
    CommonVars.properties.getProperty("linkis.alert.sub_system_id", "10001")

  val ALERT_DEFAULT_RECEIVERS = CommonVars.properties
    .getProperty("linkis.alert.receiver.default", "")
    .split(",")
    .toSet[String]

  val ALERT_IMS_MAX_LINES = CommonVars[Int]("linkis.alert.content.max.lines", 8).getValue

  val TIMEOUT_INTERVALS_SECONDS =
    CommonVars[Long]("linkis.monitor.scanner.timeout.interval.seconds", 1 * 60 * 60).getValue

  val ERRORCODE_MAX_INTERVALS_SECONDS =
    CommonVars[Long]("linkis.errorcode.scanner.max.interval.seconds", 1 * 60 * 60).getValue

  val SCAN_RULE_UNFINISHED_JOB_STATUS =
    "Inited,WaitForRetry,Scheduled,Running".split(",").map(s => s.toUpperCase())

  val LINKIS_API_VERSION: CommonVars[String] =
    CommonVars[String]("linkis.monitor.bml.api.version", "v1")

  val AUTH_TOKEN_KEY: CommonVars[String] =
    CommonVars[String]("linkis.monitor.bml.auth.token.key", "Validation-Code")

  val AUTH_TOKEN_VALUE: CommonVars[String] =
    CommonVars[String]("linkis.monitor.bml.auth.token.value", "BML-AUTH")

  val CONNECTION_MAX_SIZE: CommonVars[Int] =
    CommonVars[Int]("linkis.monitor.bml.connection.max.size", 10)

  val CONNECTION_TIMEOUT: CommonVars[Int] =
    CommonVars[Int]("linkis.monitor.bml.connection.timeout", 5 * 60 * 1000)

  val CONNECTION_READ_TIMEOUT: CommonVars[Int] =
    CommonVars[Int]("linkis.monitor.bml.connection.read.timeout", 10 * 60 * 1000)

  val AUTH_TOKEN_KEY_SHORT_NAME = "tokenKey"
  val AUTH_TOKEN_VALUE_SHORT_NAME = "tokenValue"
  val CONNECTION_MAX_SIZE_SHORT_NAME = "maxConnection"
  val CONNECTION_TIMEOUT_SHORT_NAME = "connectTimeout"
  val CONNECTION_READ_TIMEOUT_SHORT_NAME = "readTimeout"
  val CLIENT_NAME_SHORT_NAME = "clientName"
  val USER_LABEL_MONITOR = "jobhistory.label.monitor.im."

  val USER_LABEL_TENANT: CommonVars[String] =
    CommonVars[String]("linkis.monitor.jobhistory.userLabel.tenant", "{}")

  val USER_RESOURCE_MONITOR = "user.mode.monitor.im."
  val BML_CLEAR_IM = "bml.clear.monitor.im."
  val THREAD_TIME_OUT_IM = "thread.monitor.timeout.im."
  val JOB_RESULT_IM = "jobhistory.result.monitor.im."

  val BML_VERSION_MAX_NUM: CommonVars[Int] =
    CommonVars[Int]("linkis.monitor.bml.cleaner.version.max.num", 50)

  val BML_VERSION_KEEP_NUM: CommonVars[Int] =
    CommonVars[Int]("linkis.monitor.bml.cleaner.version.keep.num", 20)

  val BML_PREVIOUS_INTERVAL_TIME_DAYS: CommonVars[Long] =
    CommonVars[Long]("linkis.monitor.bml.cleaner.previous.interval.days", 30)

  val BML_CLEAN_ONCE_RESOURCE_LIMIT_NUM: CommonVars[Int] =
    CommonVars[Int]("linkis.monitor.bml.cleaner.once.limit.num", 100)

  val BML_TRASH_PATH_PREFIX: CommonVars[String] =
    CommonVars[String]("linkis.monitor.bml.trash.prefix.path", "hdfs:///tmp/linkis/trash/bml_trash")

}
