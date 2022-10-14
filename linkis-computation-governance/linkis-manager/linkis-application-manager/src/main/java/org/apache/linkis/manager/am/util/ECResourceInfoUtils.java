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
package org.apache.linkis.manager.am.util;

import org.apache.linkis.common.ServiceInstance;
import org.apache.linkis.common.utils.ByteTimeUtils;
import org.apache.linkis.governance.common.conf.GovernanceCommonConf;
import org.apache.linkis.manager.am.vo.ResourceVo;
import org.apache.linkis.manager.common.entity.enumeration.NodeStatus;
import org.apache.linkis.manager.common.entity.node.AMEMNode;
import org.apache.linkis.manager.common.entity.node.AMEngineNode;
import org.apache.linkis.manager.common.entity.persistence.ECResourceInfoRecord;
import org.apache.linkis.server.BDPJettyServerHelper;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ECResourceInfoUtils {

  private static Logger logger = LoggerFactory.getLogger(ECResourceInfoUtils.class);

  public static String NAME_REGEX = "^[a-zA-Z\\d_\\.]+$";

  public static boolean checkNameValid(String creator) {
    return Pattern.compile(NAME_REGEX).matcher(creator).find();
  }

  public static String strCheckAndDef(String str, String def) {
    return StringUtils.isBlank(str) ? def : str;
  }

  public static ResourceVo getStringToMap(String str, ECResourceInfoRecord info) {
    ResourceVo resourceVo = null;
    Map<String, Object> map = BDPJettyServerHelper.gson().fromJson(str, new HashMap<>().getClass());
    if (MapUtils.isNotEmpty(map)) {
      resourceVo = new ResourceVo();
      if (info.getLabelValue().contains("spark") || (info.getLabelValue().contains("flink"))) {
        if (null != map.get("driver")) {
          Map<String, Object> divermap = MapUtils.getMap(map, "driver");
          resourceVo.setInstance(((Double) divermap.get("instance")).intValue());
          resourceVo.setCores(((Double) divermap.get("cpu")).intValue());
          resourceVo.setMemory(ByteTimeUtils.byteStringAsBytes(divermap.get("memory").toString()));
          return resourceVo;
        } else {
          logger.warn("Compatible with old data ,{},{}", info.getLabelValue(), info);
          return null; // Compatible with old data
        }
      }
      resourceVo.setInstance(((Double) map.get("instance")).intValue());
      resourceVo.setMemory(ByteTimeUtils.byteStringAsBytes((map.get("memory").toString())));
      Double core = null == map.get("cpu") ? (Double) map.get("cores") : (Double) map.get("cpu");
      resourceVo.setCores(core.intValue());
    }
    return resourceVo;
  }

  public static AMEngineNode convertECInfoTOECNode(ECResourceInfoRecord ecInfo) {
    AMEngineNode engineNode = new AMEngineNode();
    AMEMNode ecmNode = new AMEMNode();
    ServiceInstance ecmInstance = new ServiceInstance();
    ecmInstance.setApplicationName(
        GovernanceCommonConf.ENGINE_CONN_MANAGER_SPRING_NAME().getValue());
    ecmInstance.setInstance(ecInfo.getEcmInstance());
    ecmNode.setServiceInstance(ecmInstance);
    engineNode.setEMNode(ecmNode);
    ServiceInstance ecInstance = new ServiceInstance();
    ecInstance.setInstance(ecInfo.getServiceInstance());
    ecInstance.setApplicationName(GovernanceCommonConf.ENGINE_CONN_SPRING_NAME().getValue());
    engineNode.setServiceInstance(ecInstance);
    engineNode.setOwner(ecInfo.getCreateUser());
    engineNode.setNodeStatus(NodeStatus.valueOf(ecInfo.getStatus()));
    engineNode.setTicketId(ecInfo.getTicketId());
    engineNode.setStartTime(ecInfo.getCreateTime());
    engineNode.setUpdateTime(ecInfo.getReleaseTime());
    return engineNode;
  }
}
