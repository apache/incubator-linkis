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

//package com.webank.wedatasphere.linkis.jobhistory.receiver

/*import com.webank.wedatasphere.linkis.jobhistory.cache.QueryCacheService
import com.webank.wedatasphere.linkis.jobhistory.service.JobHistoryQueryService
import com.webank.wedatasphere.linkis.protocol.query.QueryProtocol
import com.webank.wedatasphere.linkis.rpc.{RPCMessageEvent, Receiver, ReceiverChooser}

import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component*/

/**

@Component
class QueryReceiverChooser extends ReceiverChooser {

  @Autowired
  private var queryService: JobHistoryQueryService = _
  @Autowired
  private var queryCacheService: QueryCacheService = _
  private var receiver: Option[QueryReceiver] = _

  @PostConstruct
  def init(): Unit = receiver = Some(new QueryReceiver(queryService, queryCacheService))

  override def chooseReceiver(event: RPCMessageEvent): Option[Receiver] = event.message match {
    case _: QueryProtocol => receiver
    case _ => None
  }
}
*/
