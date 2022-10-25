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

package org.apache.linkis.engineplugin.spark.datacalc.model;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class SinkConfig extends DataCalcPluginConfig implements Serializable {

  protected String sourceTable;

  protected String sourceQuery;

  public String getSourceTable() {
    return sourceTable;
  }

  public void setSourceTable(String sourceTable) {
    this.sourceTable = sourceTable;
  }

  public String getSourceQuery() {
    return sourceQuery;
  }

  public void setSourceQuery(String sourceQuery) {
    this.sourceQuery = sourceQuery;
  }

  @JSONField(serialize = false)
  @AssertTrue(message = "[sourceTable, sourceQuery] cannot be blank at the same time.")
  public boolean isSourceOK() {
    return StringUtils.isNotBlank(sourceTable) || StringUtils.isNotBlank(sourceQuery);
  }
}
