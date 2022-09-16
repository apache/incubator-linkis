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

package org.apache.linkis.entrance.interceptor.impl

import org.apache.linkis.common.conf.Configuration
import org.apache.linkis.common.log.LogUtils
import org.apache.linkis.common.utils.Logging
import org.apache.linkis.entrance.exception.EntranceErrorCode
import org.apache.linkis.entrance.interceptor.exception.SetTenantLabelException
import org.apache.linkis.governance.common.entity.job.JobRequest
import org.apache.linkis.governance.common.protocol.conf.{TenantRequest, TenantResponse}
import org.apache.linkis.manager.label.builder.factory.LabelBuilderFactoryContext
import org.apache.linkis.manager.label.constant.LabelKeyConstant
import org.apache.linkis.manager.label.entity.TenantLabel
import org.apache.linkis.manager.label.utils.LabelUtil
import org.apache.linkis.rpc.Sender

import org.apache.commons.lang3.StringUtils

import java.{lang, util}
import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}

object TenantData extends Logging {

  private val configCache: LoadingCache[String, String] = CacheBuilder
    .newBuilder()
    .maximumSize(1000)
    .expireAfterAccess(1, TimeUnit.MINUTES)
    .build(new CacheLoader[String, String]() {

      override def load(userCreator: String): String = {
        val sender: Sender = Sender
          .getSender(Configuration.CLOUD_CONSOLE_CONFIGURATION_SPRING_APPLICATION_NAME.getValue)
        val user = userCreator.split("-")(0)
        val creator = userCreator.split("-")(1)
        logger.info(s"load tentant data user $user creator $creator data")
        sender.ask(TenantRequest(user, creator)) match {
          case tenantResponse: TenantResponse => tenantResponse.tenant
          case _ =>
            logger.warn(s"TenantCache user $user creator $creator data loading failed")
            ""
        }
      }

    })

  def checkTenantLabel(jobRequest: JobRequest, logAppender: lang.StringBuilder): JobRequest = {
    jobRequest match {
      case requestPersistTask: JobRequest =>
        logger.info("start to checkTenantLabel")
        val labels = requestPersistTask.getLabels
        // 判断tenant 是否存在。存在则放行，不存在则进行回填
        logger.debug("check lalabels contains tenant :{} ", labels)
        if (!labels.contains(LabelKeyConstant.TENANT_KEY)) {
          // 获取user信息
          val userName = jobRequest.getSubmitUser
          // 未获取到用户信息报错
          if (StringUtils.isEmpty(userName)) {
            throw SetTenantLabelException(
              EntranceErrorCode.USER_NULL_EXCEPTION.getErrCode,
              EntranceErrorCode.USER_NULL_EXCEPTION.getDesc
            )
          }
          // 通过user-creator  获取缓存中的 tenant
          val tenant = configCache.get(LabelUtil.getUserCreatorLabel(labels).getStringValue)
          logger.info("get cache tenant {} ", tenant)
          // 缓存获取数据不为空则添加进去
          if (StringUtils.isNotBlank(tenant)) {
            val tenantLabel = LabelBuilderFactoryContext.getLabelBuilderFactory
              .createLabel[TenantLabel](LabelKeyConstant.TENANT_KEY)
            tenantLabel.setTenant(tenant)
            labels.add(tenantLabel)
            LogUtils.generateInfo(s"Your task should be to set tenant label $tenant") + "\n"
          }
        }
      case _ => jobRequest
    }
    jobRequest
  }

}
