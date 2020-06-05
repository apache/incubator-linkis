package com.webank.wedatasphere.linkis.cs.client.http

import com.webank.wedatasphere.linkis.cs.listener.callback.imp.ContextKeyValueBean
import com.webank.wedatasphere.linkis.httpclient.dws.annotation.DWSHttpMessageResult
import com.webank.wedatasphere.linkis.httpclient.dws.response.DWSResult

/**
 * created by cooperyang on 2020/2/11
 * Description:
 */
abstract class ContextResult extends DWSResult


@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/createContextID")
class ContextCreateResult extends ContextResult {
  var contextId:String = _
  def setContextId(contextId:String):Unit = this.contextId = contextId
  def getContextId:String = this.contextId
}

@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/resetValue")
class ContextResetResult extends ContextResult {

}

@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/setValueByKey")
class ContextUpdateResult extends ContextResult {

}
@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/getContext")
class ContextGetResult extends ContextResult {
  var contextId:String = _
  var contextKeyValues:java.util.Map[String, String] = _

  def setContextId(contextId:String):Unit = this.contextId = contextId
  def getContextId:String = this.contextId

  def setContextKeyValues(kvs:java.util.Map[String,String]):Unit = this.contextKeyValues = kvs
  def getContextKeyValues:java.util.Map[String, String] = this.contextKeyValues

}


@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/removeValue")
class ContextRemoveResult extends ContextResult {

}



@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/heartbeat")
class ContextHeartBeatResult extends ContextResult {
  import java.util
  var contextKeyValueBeans:util.List[ContextKeyValueBean] = _
  def setContextKeyValueBeans(kvs:util.List[ContextKeyValueBean]):Unit = this.contextKeyValueBeans= kvs
  def getContextKeyValueBeans:util.List[ContextKeyValueBean] = this.contextKeyValueBeans;
}




@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/setValue")
class ContextSetKeyValueResult extends ContextResult {
}


@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/removeAllValue")
class ContextResetIDResult extends ContextResult {
}


@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/removeValue")
class ContextRemoveValueResult extends ContextResult {
}

@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/onBindIDListener")
class ContextBindIDResult extends ContextResult {
}


@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/onBindKeyListener")
class ContextBindKeyResult extends ContextResult {
}

@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/searchContextValue")
class ContextSearchResult extends ContextResult {
}


@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/getContextValue")
class ContextGetValueResult extends ContextResult {
}

// TODO: 用来匹配所有的void 不需要处理返回值的result
@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/(createHistory|removeHistory|removeAllValueByKeyPrefix|removeAllValueByKeyPrefixAndContextType)")
class VoidResult extends ContextResult {
}

@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/(getHistories|searchHistory)")
class ContextHistoriesGetResult extends ContextResult {
  var contextHistory:java.util.List[String] = _
  def getContextHistory:java.util.List[String] = this.contextHistory
  def setContextHistory(contextHistory:java.util.List[String])= this.contextHistory = contextHistory
}

@DWSHttpMessageResult("/api/rest_j/v\\d+/contextservice/getHistory")
class ContextHistoryGetResult extends ContextResult {
  var contextHistory:String = _
  def getContextHistory:String = this.contextHistory
  def setContextHistory(contextHistory:String)= this.contextHistory = contextHistory
}


