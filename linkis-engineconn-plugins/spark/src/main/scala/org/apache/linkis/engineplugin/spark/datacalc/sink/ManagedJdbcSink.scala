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

package org.apache.linkis.engineplugin.spark.datacalc.sink

import org.apache.linkis.common.utils.Logging
import org.apache.linkis.engineplugin.spark.datacalc.api.DataCalcSink
import org.apache.linkis.engineplugin.spark.datacalc.exception.DataSourceNotConfigException
import org.apache.linkis.engineplugin.spark.datacalc.model.DataCalcDataSource
import org.apache.linkis.engineplugin.spark.datacalc.service.LinkisDataSourceService
import org.apache.linkis.engineplugin.spark.errorcode.SparkErrorCodeSummary

import org.apache.spark.sql.{Dataset, Row, SparkSession}

import java.text.MessageFormat

class ManagedJdbcSink extends DataCalcSink[ManagedJdbcSinkConfig] with Logging {

  def output(spark: SparkSession, ds: Dataset[Row]): Unit = {
    val db: DataCalcDataSource = LinkisDataSourceService.getDatasource(config.getTargetDatasource)
    if (db == null) {
      throw new DataSourceNotConfigException(
        SparkErrorCodeSummary.DATA_CALC_DATASOURCE_NOT_CONFIG.getErrorCode,
        MessageFormat.format(
          SparkErrorCodeSummary.DATA_CALC_DATASOURCE_NOT_CONFIG.getErrorDesc,
          config.getTargetDatasource
        )
      )
    }

    val jdbcConfig = new JdbcSinkConfig()
    jdbcConfig.setUrl(db.getUrl)
    jdbcConfig.setDriver(db.getDriver)
    jdbcConfig.setUser(db.getUser)
    jdbcConfig.setPassword(db.getPassword)
    jdbcConfig.setTargetDatabase(config.getTargetDatabase)
    jdbcConfig.setTargetTable(config.getTargetTable)
    jdbcConfig.setSaveMode(config.getSaveMode)
    jdbcConfig.setPreQueries(config.getPreQueries)
    jdbcConfig.setNumPartitions(config.getNumPartitions)
    jdbcConfig.setOptions(config.getOptions)
    jdbcConfig.setSourceTable(config.getSourceTable)
    jdbcConfig.setSourceQuery(config.getSourceQuery)
    jdbcConfig.setVariables(config.getVariables)

    val sinkPlugin = new JdbcSink()
    sinkPlugin.setConfig(jdbcConfig)
    sinkPlugin.output(spark, ds)
  }

}
