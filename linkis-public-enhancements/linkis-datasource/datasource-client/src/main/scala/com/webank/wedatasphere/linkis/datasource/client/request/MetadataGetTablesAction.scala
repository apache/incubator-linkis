package com.webank.wedatasphere.linkis.datasource.client.request

import com.webank.wedatasphere.linkis.datasource.client.exception.DataSourceClientBuilderException
import com.webank.wedatasphere.linkis.httpclient.request.GetAction

class MetadataGetTablesAction extends GetAction with DataSourceAction {
  private var dataSourceId: Long = _
  private var database: String = _

  override def suffixURLs: Array[String] = Array("metadata", "tables", dataSourceId.toString, "db", database)
}

object MetadataGetTablesAction {
  def builder(): Builder = new Builder

  class Builder private[MetadataGetTablesAction]() {
    private var dataSourceId: Long = _
    private var database: String = _
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

    def setDatabase(database: String): Builder = {
      this.database = database
      this
    }

    def setSystem(system: String): Builder = {
      this.system = system
      this
    }

    def build(): MetadataGetTablesAction = {
      if(dataSourceId == null) throw new DataSourceClientBuilderException("dataSourceId is needed!")
      if(database == null) throw new DataSourceClientBuilderException("database is needed!")
      if(system == null) throw new DataSourceClientBuilderException("system is needed!")
      if(user == null) throw new DataSourceClientBuilderException("user is needed!")

      val metadataGetTablesAction = new MetadataGetTablesAction
      metadataGetTablesAction.dataSourceId = this.dataSourceId
      metadataGetTablesAction.database = this.database
      metadataGetTablesAction.setParameter("system", system)
      metadataGetTablesAction.setUser(user)
      metadataGetTablesAction
    }
  }

}
