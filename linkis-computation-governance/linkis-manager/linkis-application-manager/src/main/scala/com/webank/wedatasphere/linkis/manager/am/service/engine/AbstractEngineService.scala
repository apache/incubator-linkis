package com.webank.wedatasphere.linkis.manager.am.service.engine

import com.webank.wedatasphere.linkis.manager.am.manager.EngineNodeManager
import com.webank.wedatasphere.linkis.manager.am.service.{EMEngineService, EngineService}
import org.springframework.beans.factory.annotation.Autowired


abstract class AbstractEngineService extends EngineService {

  @Autowired
  private var emService: EMEngineService = _

  @Autowired
  private var engineNodeManager:EngineNodeManager = _

  override def getEMService(): EMEngineService = {
    this.emService
  }

  override def getEngineNodeManager: EngineNodeManager = {
    this.engineNodeManager
  }
}
