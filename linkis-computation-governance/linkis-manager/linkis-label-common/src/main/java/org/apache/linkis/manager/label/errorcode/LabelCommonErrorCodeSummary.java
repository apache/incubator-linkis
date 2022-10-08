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

package org.apache.linkis.manager.label.errorcode;

public enum LabelCommonErrorCodeSummary {
  UPDATE_LABEL_FAILED(
      40001, "update label realtion failed(更新标签属性失败)", "update label realtion failed(更新标签属性失败)"),
  LABEL_ERROR_CODE(
      40001,
      "The value of the label is set incorrectly, only one value can be set, and the symbol cannot be used(标签的值设置错误,只能设置一个值，符号不能用) ",
      "The value of the label is set incorrectly, only one value can be set, and the symbol cannot be used(标签的值设置错误,只能设置一个值，符号不能用) "),
  FAILED_BUILD_COMBINEDLABEL(
      40001, "Failed to build combinedLabel构建组合标签失败) ", "Failed to build combinedLabel(构建组合标签失败) "),
  FAILED_READ_INPUT_STREAM(
      40001,
      "Fail to read value input stream(读取值输入流失败) ",
      "Fail to read value input stream(读取值输入流失败) "),
  FAILED_CONSTRUCT_INSTANCE(
      40001,
      "Fail to construct a label instance of(未能构建标签实例):",
      "Fail to construct a label instance of(未能构建标签实例):"),
  NOT_SUPPORT_ENVTYPE(
      40001, "Not support envType(不支持 envType):", "Not support envType(不支持 envType):"),
  CHECK_LABEL_REMOVE_REQUEST(
      130001,
      "ServiceInstance in request is null, please check label remove request(请求中的 ServiceInstance 为空，请检查标签删除请求)",
      "ServiceInstance in request is null, please check label remove request(请求中的 ServiceInstance 为空，请检查标签删除请求)");

  /** (errorCode)错误码 */
  private int errorCode;
  /** (errorDesc)错误描述 */
  private String errorDesc;
  /** Possible reasons for the error(错误可能出现的原因) */
  private String comment;

  LabelCommonErrorCodeSummary(int errorCode, String errorDesc, String comment) {
    this.errorCode = errorCode;
    this.errorDesc = errorDesc;
    this.comment = comment;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorDesc() {
    return errorDesc;
  }

  public void setErrorDesc(String errorDesc) {
    this.errorDesc = errorDesc;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return "errorCode: " + this.errorCode + ", errorDesc:" + this.errorDesc;
  }
}
