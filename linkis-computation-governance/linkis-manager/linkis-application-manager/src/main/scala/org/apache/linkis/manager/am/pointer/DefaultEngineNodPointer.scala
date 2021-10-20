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
 
package org.apache.linkis.manager.am.pointer

import org.apache.linkis.manager.common.entity.node.Node
import org.apache.linkis.manager.common.protocol.{RequestEngineLock, RequestEngineUnlock, ResponseEngineLock}
import org.apache.linkis.manager.service.common.pointer.EngineNodePointer


class DefaultEngineNodPointer(val node: Node) extends AbstractNodePointer with EngineNodePointer {


  /**
    * 与该远程指针关联的node信息
    *
    * @return
    */
  override def getNode(): Node = node

  override def lockEngine(requestEngineLock: RequestEngineLock): Option[String] = {
    getSender.ask(requestEngineLock) match {
      case ResponseEngineLock(lockStatus, lock, msg) =>
        if (lockStatus) {
          Some(lock)
        } else {
          error("Failed to get locker: " + msg)
          None
        }
      case _ => None
    }
  }

  override def releaseLock(requestEngineUnlock: RequestEngineUnlock): Unit = {
    getSender.send(requestEngineUnlock)
  }
}
