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

package com.webank.wedatasphere.linkis.engineconnplugin.sqoop

import com.webank.wedatasphere.linkis.engineconnplugin.sqoop.factory.SqoopEngineConnFactory
import com.webank.wedatasphere.linkis.engineconnplugin.sqoop.launch.SqoopEngineConnLaunchBuilder
import com.webank.wedatasphere.linkis.engineconnplugin.sqoop.resource.SqoopEngineConnResourceFactory
import com.webank.wedatasphere.linkis.manager.engineplugin.common.EngineConnPlugin
import com.webank.wedatasphere.linkis.manager.engineplugin.common.creation.EngineConnFactory
import com.webank.wedatasphere.linkis.manager.engineplugin.common.launch.EngineConnLaunchBuilder
import com.webank.wedatasphere.linkis.manager.engineplugin.common.resource.EngineResourceFactory
import com.webank.wedatasphere.linkis.manager.label.entity.Label


class SqoopEngineConnPlugin extends EngineConnPlugin{
  private val EP_CONTEXT_CONSTRUCTOR_LOCK = new Object()
  private var engineResourceFactory: EngineResourceFactory = _
  private var engineConnLaunchBuilder: EngineConnLaunchBuilder = _
  private var engineConnFactory: EngineConnFactory = _
  override def init(params: java.util.Map[String, Any]): Unit = {}

  override def getEngineResourceFactory: EngineResourceFactory = {

    EP_CONTEXT_CONSTRUCTOR_LOCK.synchronized{
      if(null == engineResourceFactory){
        engineResourceFactory = new SqoopEngineConnResourceFactory
      }
      engineResourceFactory
    }
  }

  override def getEngineConnLaunchBuilder: EngineConnLaunchBuilder = {
    EP_CONTEXT_CONSTRUCTOR_LOCK.synchronized {
      if (null == engineConnLaunchBuilder) {
        engineConnLaunchBuilder = new SqoopEngineConnLaunchBuilder()
      }
      engineConnLaunchBuilder
    }
  }


  override def getEngineConnFactory: EngineConnFactory = {
    EP_CONTEXT_CONSTRUCTOR_LOCK.synchronized {
      if (null == engineConnFactory) {
        engineConnFactory = new SqoopEngineConnFactory
      }
      engineConnFactory
    }
  }

  override def getDefaultLabels: java.util.List[Label[_]] = new java.util.ArrayList[Label[_]]
}
