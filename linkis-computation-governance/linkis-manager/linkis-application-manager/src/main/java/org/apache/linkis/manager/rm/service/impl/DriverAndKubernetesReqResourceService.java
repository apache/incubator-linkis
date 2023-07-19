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

package org.apache.linkis.manager.rm.service.impl;

import org.apache.linkis.manager.common.entity.resource.*;
import org.apache.linkis.manager.common.exception.RMWarnException;
import org.apache.linkis.manager.rm.domain.RMLabelContainer;
import org.apache.linkis.manager.rm.external.kubernetes.KubernetesResourceIdentifier;
import org.apache.linkis.manager.rm.external.service.ExternalResourceService;
import org.apache.linkis.manager.rm.service.LabelResourceService;
import org.apache.linkis.manager.rm.service.RequestResourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.linkis.manager.common.entity.resource.ResourceType.DriverAndKubernetes;

public class DriverAndKubernetesReqResourceService extends RequestResourceService {
  private static final Logger logger =
      LoggerFactory.getLogger(DriverAndKubernetesReqResourceService.class);
  private LabelResourceService labelResourceService;
  private ExternalResourceService externalResourceService;
  private final ResourceType resourceType = DriverAndKubernetes;

  public DriverAndKubernetesReqResourceService(
      LabelResourceService labelResourceService, ExternalResourceService externalResourceService) {
    super(labelResourceService);
    this.labelResourceService = labelResourceService;
    this.externalResourceService = externalResourceService;
  }

  @Override
  public ResourceType resourceType() {
    return this.resourceType;
  }

  @Override
  public boolean canRequest(RMLabelContainer labelContainer, NodeResource resource)
      throws RMWarnException {
    //        if (!super.canRequest(labelContainer, resource)) {
    //          return false;
    //        }
    DriverAndKubernetesResource requestedDriverAndKubernetesResource =
        (DriverAndKubernetesResource) resource.getMaxResource();
    KubernetesResource requestedKubernetesResource =
        requestedDriverAndKubernetesResource.getKubernetesResource();

    KubernetesResourceIdentifier kubernetesResourceIdentifier = new KubernetesResourceIdentifier();
    NodeResource providedKubernetesResource =
        externalResourceService.getResource(
            ResourceType.Kubernetes, labelContainer, kubernetesResourceIdentifier);
    Resource maxCapacity = providedKubernetesResource.getMaxResource();
    Resource usedCapacity = providedKubernetesResource.getUsedResource();

    logger.info(
        "Kubernetes resource: used resource:" + usedCapacity + " and max resource: " + maxCapacity);
    Resource leftResource = maxCapacity.minus(usedCapacity);
    logger.info(
        "Kubernetes resource: left "
            + leftResource
            + ", this request requires: "
            + requestedKubernetesResource);
    if (leftResource.less(requestedKubernetesResource)) {
      logger.info(
          "user: "
              + labelContainer.getUserCreatorLabel().getUser()
              + " request kubernetes resource "
              + requestedKubernetesResource
              + " > left resource "
              + leftResource);
      //          Pair<Integer, String> notEnoughMessage =
      //              generateQueueNotEnoughMessage(requestedYarnResource, queueLeftResource,
      // maxCapacity);
      //          throw new RMWarnException(notEnoughMessage.getKey(), notEnoughMessage.getValue());
      return false;
    } else {
      return true;
    }
  }
}
