package com.webank.wedatasphere.linkis.datasource.client.response

import com.webank.wedatasphere.linkis.datasourcemanager.common.domain.DataSourceType
import com.webank.wedatasphere.linkis.httpclient.dws.DWSHttpClient
import com.webank.wedatasphere.linkis.httpclient.dws.annotation.DWSHttpMessageResult
import com.webank.wedatasphere.linkis.httpclient.dws.response.DWSResult

import java.util
import scala.beans.BeanProperty

@DWSHttpMessageResult("/api/rest_j/v\\d+/datasource/type/all")
class GetAllDataSourceTypesResult extends DWSResult{
  @BeanProperty var type_list: java.util.List[java.util.Map[String, Any]] = _

  def getAllDataSourceType: util.List[DataSourceType]={
    import scala.collection.JavaConverters._

    type_list.asScala.map(x=>{
      val str = DWSHttpClient.jacksonJson.writeValueAsString(x)
      DWSHttpClient.jacksonJson.readValue(str, classOf[DataSourceType])
    }).asJava
  }
}
