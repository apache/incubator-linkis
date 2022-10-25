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

package org.apache.linkis.engineplugin.spark.datacalc.sink;

import org.apache.linkis.engineplugin.spark.datacalc.model.SinkConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcSinkConfig extends SinkConfig {

  @NotBlank private String url;

  @NotBlank private String driver;

  @NotBlank private String user;

  @NotBlank private String password;

  @NotBlank private String targetTable;

  @NotBlank
  @Pattern(
      regexp = "^(overwrite|append|ignore|error|errorifexists)$",
      message =
          "Unknown save mode: {saveMode}. Accepted save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists'.")
  private String saveMode = "overwrite";

  private List<String> preQueries = new ArrayList<>();

  private Integer numPartitions = 10;

  private Map<String, String> options;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getTargetTable() {
    return targetTable;
  }

  public void setTargetTable(String targetTable) {
    this.targetTable = targetTable;
  }

  public String getSaveMode() {
    return saveMode;
  }

  public void setSaveMode(String saveMode) {
    this.saveMode = saveMode;
  }

  public List<String> getPreQueries() {
    return preQueries;
  }

  public void setPreQueries(List<String> preQueries) {
    this.preQueries = preQueries;
  }

  public Integer getNumPartitions() {
    return numPartitions;
  }

  public void setNumPartitions(Integer numPartitions) {
    if (numPartitions == null) return;
    this.numPartitions = numPartitions > 20 ? 20 : numPartitions;
  }

  public Map<String, String> getOptions() {
    return options;
  }

  public void setOptions(Map<String, String> options) {
    this.options = options;
  }
}
