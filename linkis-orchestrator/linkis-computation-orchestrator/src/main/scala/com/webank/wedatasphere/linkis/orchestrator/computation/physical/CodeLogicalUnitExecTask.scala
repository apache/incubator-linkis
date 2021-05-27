package com.webank.wedatasphere.linkis.orchestrator.computation.physical

import java.util.concurrent.TimeUnit

import com.webank.wedatasphere.linkis.common.exception.{ErrorException, LinkisRetryException, WarnException}
import com.webank.wedatasphere.linkis.common.utils.{Logging, Utils}
import com.webank.wedatasphere.linkis.governance.common.protocol.task.{RequestTask, RequestTaskExecute}
import com.webank.wedatasphere.linkis.governance.common.utils.GovernanceConstant
import com.webank.wedatasphere.linkis.manager.label.entity.Label
import com.webank.wedatasphere.linkis.orchestrator.computation.conf.ComputationOrchestratorConf
import com.webank.wedatasphere.linkis.orchestrator.computation.execute.{CodeExecTaskExecutor, CodeExecTaskExecutorManager}
import com.webank.wedatasphere.linkis.orchestrator.exception.{OrchestratorErrorCodeSummary, OrchestratorErrorException, OrchestratorRetryException}
import com.webank.wedatasphere.linkis.orchestrator.execution.AsyncTaskResponse.NotifyListener
import com.webank.wedatasphere.linkis.orchestrator.execution.impl.DefaultFailedTaskResponse
import com.webank.wedatasphere.linkis.orchestrator.execution.{AsyncTaskResponse, TaskResponse}
import com.webank.wedatasphere.linkis.orchestrator.plans.ast.QueryParams
import com.webank.wedatasphere.linkis.orchestrator.plans.physical.{AbstractExecTask, ExecTask, PhysicalContext, ReheatableExecTask, RetryExecTask}
import com.webank.wedatasphere.linkis.orchestrator.plans.unit.CodeLogicalUnit
import com.webank.wedatasphere.linkis.orchestrator.strategy.async.AsyncExecTask
import com.webank.wedatasphere.linkis.orchestrator.strategy.{ResultSetExecTask, StatusInfoExecTask}
import com.webank.wedatasphere.linkis.orchestrator.utils.OrchestratorIDCreator
import com.webank.wedatasphere.linkis.scheduler.executer.{ErrorExecuteResponse, SubmitResponse}
import org.apache.commons.lang.StringUtils

import scala.concurrent.duration.Duration
import scala.collection.convert.decorateAsScala._
/**
  *
  *
  */
class CodeLogicalUnitExecTask (parents: Array[ExecTask], children: Array[ExecTask])
  extends AbstractExecTask(parents, children) with StatusInfoExecTask with ResultSetExecTask
    with AsyncExecTask with Logging {


  private var physicalContext: PhysicalContext = _

  private var id: String = _

  private val codeExecTaskExecutorManager = CodeExecTaskExecutorManager.getCodeExecTaskExecutorManager
  private val askDuration  = Duration(ComputationOrchestratorConf.MAX_ASK_EXECUTOR_TIME.getValue.toLong, TimeUnit.MILLISECONDS)
  private var codeLogicalUnit: CodeLogicalUnit = _

  override def execute(): TaskResponse = {
    info(s"Start to execute CodeLogicalUnitExecTask(${getIDInfo()}).")
    var executor: Option[CodeExecTaskExecutor] = None
    var retryException: LinkisRetryException = null
    executor = Utils.tryCatch(codeExecTaskExecutorManager.askExecutor(this, askDuration)) {
      case retry: LinkisRetryException =>
        retryException = retry
        None
      case e:ErrorException =>
        //job.getLogListener.foreach(_.onLogUpdate(job, LogUtils.generateERROR(e.getMessage)))
        throw e
      case error: Throwable =>
        //job.getLogListener.foreach(_.onLogUpdate(job, LogUtils.generateERROR(error.getMessage)))
        throw error
    }

    if (executor.isDefined) {
      val requestTask = toRequestTask
      val codeExecutor = executor.get
      val response = codeExecutor.getEngineConnExecutor.execute(requestTask)
      response match {
        case SubmitResponse(engineConnExecId) =>
          //封装engineConnExecId信息
          codeExecutor.setEngineConnTaskId(engineConnExecId)
          codeExecTaskExecutorManager.addEngineConnTaskID(codeExecutor)
          new AsyncTaskResponse {
            override def notifyMe(listener: NotifyListener): Unit = null

            override def waitForCompleted(): TaskResponse = throw new OrchestratorErrorException(OrchestratorErrorCodeSummary.METHOD_NUT_SUPPORT_CODE, "waitForCompleted method not support")
          }
        case ErrorExecuteResponse(message, t) =>
          info(s"failed to submit task to engineConn,reason: $message")
          throw new OrchestratorRetryException(OrchestratorErrorCodeSummary.EXECUTION_FOR_EXECUTION_ERROR_CODE, "failed to submit task to engineConn", t)
      }
    } else if(null != retryException) {

      new DefaultFailedTaskResponse(s"ask Engine failed + ${retryException.getMessage}", OrchestratorErrorCodeSummary.EXECUTION_FOR_EXECUTION_ERROR_CODE, retryException)
    } else {
      throw new OrchestratorRetryException(OrchestratorErrorCodeSummary.EXECUTION_FOR_EXECUTION_ERROR_CODE, "Failed to ask executor")
    }

  }

  private def toRequestTask: RequestTask ={
    val requestTask = new RequestTaskExecute
    requestTask.setCode(getCodeLogicalUnit.toStringCode)
    getLabels.add(getCodeLogicalUnit.getLabel)
    requestTask.setLabels(getLabels)
    //Map
//    if (null != getParams.getRuntimeParams.getDataSources ) {
//      requestTask.getProperties.putAll(getParams.getRuntimeParams.getDataSources)
//    }

//    if (null != getParams.getRuntimeParams.getContext) {
//      requestTask.getProperties.putAll(getParams.getRuntimeParams.getContext)
//    }

//    if (null != getParams.getRuntimeParams.getSpecials) {
//      requestTask.getProperties.putAll(getParams.getRuntimeParams.getSpecials)
//    }

    if (null != getParams.getRuntimeParams.getJobs) {
      requestTask.getProperties.putAll(getParams.getRuntimeParams.getJobs)
   }
//    requestTask.getProperties.put(GovernanceConstant.TASK_SOURCE_MAP_KEY, getParams.getRuntimeParams.get(GovernanceConstant.TASK_SOURCE_MAP_KEY) match {
//      case o: Object => o
//      case _ => null
//    })
    requestTask.getProperties.putAll(getParams.getRuntimeParams.toMap)
    requestTask
  }

  override def isLocalMode: Boolean = false

  override def canExecute: Boolean = true

  override def verboseString: String = s"CodeLogicalUnitExecTask(codes=${codeLogicalUnit.codes.toArray.mkString(";")}, labels=${getLabels.asScala.map(_.getStringValue).mkString("&")})"

  override def initialize(physicalContext: PhysicalContext): Unit = {
    this.physicalContext = physicalContext
    //CodeLogicalUnitExecTask is an executable task which has progress information, so initialize it as 0.0

  }

  override def getId: String = {
    if (null == id) synchronized {
      if (null == id) {
        id = OrchestratorIDCreator.getPhysicalTaskIDCreator.nextID("codeExec")
      }
    }
    id
  }

  override def getPhysicalContext: PhysicalContext = physicalContext

  def getCodeLogicalUnit: CodeLogicalUnit = this.codeLogicalUnit

  def setCodeLogicalUnit(codeLogicalUnit: CodeLogicalUnit): Unit = this.codeLogicalUnit = codeLogicalUnit

  def getParams: QueryParams = {
    getTaskDesc.getOrigin.getASTOrchestration.getASTContext.getParams
  }

  def getLabels: java.util.List[Label[_]] = {
    val labels = getTaskDesc.getOrigin.getASTOrchestration.getASTContext.getLabels
    if (null != labels) {
      labels
    } else {
      null
    }
  }


  def getExecuteUser: String = {
    getTaskDesc.getOrigin.getASTOrchestration.getASTContext.getExecuteUser
  }

  override def kill(): Unit = {
    codeExecTaskExecutorManager.getByExecTaskId(this.getId).foreach { codeEngineConnExecutor =>
      if (StringUtils.isNotBlank(codeEngineConnExecutor.getEngineConnTaskId)) {
        info(s"execTask($getId) be killed, engineConn execId is${codeEngineConnExecutor.getEngineConnTaskId}")
        Utils.tryAndWarn(codeEngineConnExecutor.getEngineConnExecutor.killTask(codeEngineConnExecutor.getEngineConnTaskId))
        Utils.tryAndWarn(codeExecTaskExecutorManager.unLockEngineConn(this, codeEngineConnExecutor))
      }
    }
  }

  def getCodeEngineConnExecutor: CodeExecTaskExecutor = {
    codeExecTaskExecutorManager.getByExecTaskId(this.getId).orNull
  }

  override protected def newNode(): ExecTask = {
    val task = new CodeLogicalUnitExecTask(null, null)
    task.setCodeLogicalUnit(codeLogicalUnit)
    task.setTaskDesc(getTaskDesc)
    task
  }

  /*override def close(): Unit = {
    codeExecTaskExecutorManager.getByExecTaskId(this.getId).foreach { codeEngineConnExecutor =>
      info(s"ExecTask(${getIDInfo()}) be closed.")
      Utils.tryAndWarn(codeExecTaskExecutorManager.delete(this, codeEngineConnExecutor))
    }
  }*/
  override def clear(isSucceed: Boolean): Unit = {

    codeExecTaskExecutorManager.getByExecTaskId(this.getId).foreach { codeEngineConnExecutor =>
      if (isSucceed) {
        info(s"ExecTask(${getIDInfo()}) execute  success executor be delete.")
        Utils.tryAndWarn(codeExecTaskExecutorManager.delete(this, codeEngineConnExecutor))
      } else {
        info(s"ExecTask(${getIDInfo()}) execute  failed executor be unLock.")
        Utils.tryAndWarn(codeExecTaskExecutorManager.unLockEngineConn(this, codeEngineConnExecutor))
      }
    }
  }
}

