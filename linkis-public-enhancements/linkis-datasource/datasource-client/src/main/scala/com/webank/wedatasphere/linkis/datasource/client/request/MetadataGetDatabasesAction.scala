package com.webank.wedatasphere.linkis.datasource.client.request

import com.webank.wedatasphere.linkis.datasource.client.exception.DataSourceClientBuilderException
import com.webank.wedatasphere.linkis.httpclient.request.GetAction

class MetadataGetDatabasesAction extends GetAction with DataSourceAction {
  private var dataSourceId: Long = _

  override def suffixURLs: Array[String] = Array("metadata", "dbs", dataSourceId.toString)
}

object MetadataGetDatabasesAction {
  def builder(): Builder = new Builder

  class Builder private[MetadataGetDatabasesAction]() {
    private var dataSourceId: Long = _
    private var system:String = _
    private var user: String = _

    def setUser(user: String): Builder = {
      this.user = user
      this
    }

    def setDataSourceId(dataSourceId: Long): Builder = {
      this.dataSourceId = dataSourceId
      this
    }

    def setSystem(system: String): Builder = {
      this.system = system
      this
    }

    def build(): MetadataGetDatabasesAction = {
      if(dataSourceId == null) throw new DataSourceClientBuilderException("dataSourceId is needed!")
      if(system == null) throw new DataSourceClientBuilderException("system is needed!")
      if(user == null) throw new DataSourceClientBuilderException("user is needed!")

      val metadataGetDatabasesAction = new MetadataGetDatabasesAction
      metadataGetDatabasesAction.dataSourceId = this.dataSourceId
      metadataGetDatabasesAction.setParameter("system", system)
      metadataGetDatabasesAction.setUser(user)
      metadataGetDatabasesAction
    }
  }

}
