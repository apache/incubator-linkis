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

package org.apache.linkis.entrance.execute

import org.apache.linkis.governance.common.utils.JobUtils
import org.apache.linkis.orchestrator.execution.TaskResponse
import org.apache.linkis.orchestrator.plans.ast.{ASTContext, Job, QueryParams, Stage}
import org.apache.linkis.orchestrator.utils.OrchestratorIDCreator

import org.apache.commons.lang3.StringUtils

/**
 */
trait ExecTask {

  private var idInfo: String = _

  private var id: String = _

  private val idLock = new Array[Byte](0)
  private val idInfoLock = new Array[Byte](0)

  def execute(): TaskResponse

  def getQueryParams: QueryParams

  def getIDInfo(): String = {
    if (null == idInfo) idInfoLock synchronized {
      if (null == idInfo) {
        val queryParams = getQueryParams
        if (
            null != queryParams && null != queryParams.getRuntimeParams && null != queryParams.getRuntimeParams.toMap
        ) {
          val runtimeMap = queryParams.getRuntimeParams.toMap
          val taskId = JobUtils.getJobIdFromMap(runtimeMap)
          if (StringUtils.isNotBlank(taskId)) {
            idInfo = s"TaskID_${taskId}_otJobId_${getId}"
          } else {
            idInfo = getId
          }
        } else {
          idInfo = getId
        }
      }
    }
    idInfo
  }

  def getId: String = {
    if (null == id) idLock synchronized {
      if (null == id) {
        id = OrchestratorIDCreator.getAstJobIDCreator.nextID("astJob")
      }
    }
    id
  }

}
