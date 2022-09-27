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

package org.apache.linkis.manager.common.protocol

import org.apache.linkis.governance.common.exception.GovernanceErrorException
import org.apache.linkis.governance.errorcode.ComputationCommonErrorCodeSummary.IS_NOT_EXISTS

trait OperateRequest {
  val user: String
  val parameters: java.util.Map[String, Object]
}

object OperateRequest {

  val OPERATOR_NAME_KEY = "__operator_name__"

  def getOperationName(parameters: Map[String, Any]): String =
    parameters
      .getOrElse(
        OperateRequest.OPERATOR_NAME_KEY,
        throw new GovernanceErrorException(
          IS_NOT_EXISTS.getErrorCode,
          s"$OPERATOR_NAME_KEY is not exists."
        )
      )
      .asInstanceOf[String]

  def getOperationName(parameters: java.util.Map[String, Any]): String =
    parameters.get(OperateRequest.OPERATOR_NAME_KEY) match {
      case v: String => v
      case _ =>
        throw new GovernanceErrorException(
          IS_NOT_EXISTS.getErrorCode,
          s"$OPERATOR_NAME_KEY is not exists."
        )
    }

}
