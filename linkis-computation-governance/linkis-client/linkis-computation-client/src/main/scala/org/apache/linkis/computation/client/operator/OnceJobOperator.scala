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
 
package org.apache.linkis.computation.client.operator

import org.apache.linkis.common.ServiceInstance
import org.apache.linkis.computation.client.once.LinkisManagerClient


trait OnceJobOperator[T] extends Operator[T] {

  private var serviceInstance: ServiceInstance = _
  private var linkisManagerClient: LinkisManagerClient = _

  protected def getServiceInstance: ServiceInstance = serviceInstance
  protected def getLinkisManagerClient: LinkisManagerClient = linkisManagerClient

  def setServiceInstance(serviceInstance: ServiceInstance): this.type = {
    this.serviceInstance = serviceInstance
    this
  }

  def setLinkisManagerClient(linkisManagerClient: LinkisManagerClient): this.type = {
    this.linkisManagerClient = linkisManagerClient
    this
  }

}
