/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.wedatasphere.linkis.enginemanager.impala.conf

import java.util

import com.webank.wedatasphere.linkis.enginemanager.AbstractEngineResourceFactory
import com.webank.wedatasphere.linkis.resourcemanager.{LoadInstanceResource, Resource}
import org.springframework.stereotype.Component

@Component("engineResourceFactory")
class ImpalaEngineResourceFactory extends AbstractEngineResourceFactory{
  
  override protected def getRequestResource(properties: util.Map[String, String]): Resource = {
    if (properties.containsKey(ImpalaResourceConfiguration.IMPALA_ENGINE_REQUEST_MEMORY.key)){
      val settingClientMemory = properties.get(ImpalaResourceConfiguration.IMPALA_ENGINE_REQUEST_MEMORY.key)
      if (!settingClientMemory.toLowerCase().endsWith("g")){
        properties.put(ImpalaResourceConfiguration.IMPALA_ENGINE_REQUEST_MEMORY.key, settingClientMemory + "g")
      }
    }
    new LoadInstanceResource(
      ImpalaResourceConfiguration.IMPALA_ENGINE_REQUEST_MEMORY.getValue(properties).toLong,
      ImpalaResourceConfiguration.IMPALA_ENGINE_REQUEST_CORES.getValue(properties),
      ImpalaResourceConfiguration.IMPALA_ENGINE_REQUEST_INSTANCE.getValue(properties)
      )
  }
}
