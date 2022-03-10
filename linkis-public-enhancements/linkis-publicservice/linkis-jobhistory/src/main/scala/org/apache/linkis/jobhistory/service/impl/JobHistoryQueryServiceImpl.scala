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
 
package org.apache.linkis.jobhistory.service.impl

import java.lang
import java.sql.Timestamp
import com.google.common.collect.Iterables
import org.apache.commons.lang.StringUtils
import org.apache.linkis.common.utils.{Logging, Utils}
import org.apache.linkis.governance.common.constant.job.JobRequestConstants
import org.apache.linkis.governance.common.entity.job.{JobRequest, JobRequestWithDetail, QueryException, SubJobDetail}
import org.apache.linkis.governance.common.protocol.job._
import org.apache.linkis.jobhistory.conversions.TaskConversions._
import org.apache.linkis.jobhistory.dao.{JobDetailMapper, JobHistoryMapper}
import org.apache.linkis.jobhistory.entity.JobHistory
import org.apache.linkis.jobhistory.util.QueryUtils
import org.apache.linkis.rpc.message.annotation.Receiver
import org.apache.commons.lang.exception.ExceptionUtils
import org.apache.linkis.jobhistory.conversions.TaskConversions

import scala.collection.JavaConverters.asScalaBufferConverter
import java.util
import java.util.Date
import org.apache.linkis.jobhistory.entity.QueryJobHistory
import org.apache.linkis.jobhistory.service.JobHistoryQueryService
import org.apache.linkis.jobhistory.transitional.TaskStatus
import org.apache.linkis.manager.label.utils.LabelUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConversions._


@Service
class JobHistoryQueryServiceImpl extends JobHistoryQueryService with Logging {

  @Autowired
  private var jobHistoryMapper: JobHistoryMapper = _
  @Autowired
  private var jobDetailMapper: JobDetailMapper = _
//  @Autowired
//  private var queryCacheService: QueryCacheService = _

  @Receiver
  override def add(jobReqInsert: JobReqInsert): JobRespProtocol = {
    info("Insert data into the database(往数据库中插入数据)：" + jobReqInsert.toString)
    val jobResp = new JobRespProtocol
    Utils.tryCatch {
      QueryUtils.storeExecutionCode(jobReqInsert.jobReq)
      val jobInsert = jobRequest2JobHistory(jobReqInsert.jobReq)
      jobInsert.setUpdatedTime(jobInsert.getCreatedTime)
      jobHistoryMapper.insertJobHistory(jobInsert)
      val map = new util.HashMap[String, Object]()
      map.put(JobRequestConstants.JOB_ID, jobInsert.getId.asInstanceOf[Object])
      jobResp.setStatus(0)
      jobResp.setData(map)
    } {
      case exception: Exception =>
        logger.error(s"Failed to add JobReqInsert ${jobReqInsert.toString},should be retry", exception)
        jobResp.setStatus(2)
        jobResp.setMsg(ExceptionUtils.getRootCauseMessage(exception))
    }
    jobResp
  }

  @Receiver
  override def change(jobReqUpdate: JobReqUpdate): JobRespProtocol = {
    val jobReq = jobReqUpdate.jobReq
    jobReq.setExecutionCode(null)
    info("Update data to the database(往数据库中更新数据)：task " + jobReq.getId + "status:" + jobReq.getStatus)
    val jobResp = new JobRespProtocol
    Utils.tryCatch {
      if (jobReq.getErrorDesc != null) {
        if (jobReq.getErrorDesc.length > 256) {
          info(s"errorDesc is too long,we will cut some message")
          jobReq.setErrorDesc(jobReq.getErrorDesc.substring(0, 256))
          info(s"${jobReq.getErrorDesc}")
        }
      }
      if (jobReq.getStatus != null) {
        val oldStatus: String = jobHistoryMapper.selectJobHistoryStatusForUpdate(jobReq.getId)
        if (oldStatus != null && !shouldUpdate(oldStatus, jobReq.getStatus)) {
          throw new QueryException(120001,s"jobId:${jobReq.getId}，在数据库中的task状态为：${oldStatus}，更新的task状态为：${jobReq.getStatus}，更新失败！")
        }
      }
      val jobUpdate = jobRequest2JobHistory(jobReq)
      if(jobUpdate.getUpdatedTime == null) {
        throw new QueryException(120001,s"jobId:${jobReq.getId}，更新job相关信息失败，请指定该请求的更新时间!")
      }
      logger.info(s"Update data to the database(往数据库中更新数据)：task ${jobReq.getId} + status ${jobReq.getStatus}, updateTime: ${jobUpdate.getUpdateTimeMills}, progress : ${jobUpdate.getProgress}")
      jobHistoryMapper.updateJobHistory(jobUpdate)
      val map = new util.HashMap[String, Object]
      map.put(JobRequestConstants.JOB_ID, jobReq.getId.asInstanceOf[Object])
      jobResp.setStatus(0)
      jobResp.setData(map)
    }{
      case exception: QueryException =>
        logger.error(s"Failed to update JobReqUpdate ${jobReq.getId},status ${jobReq.getStatus}", exception)
        jobResp.setStatus(1)
        jobResp.setMsg(ExceptionUtils.getRootCauseMessage(exception))
      case exception: Exception =>
        logger.error(s"Failed to update JobReqUpdate ${jobReq.getId},status ${jobReq.getStatus}", exception)
        jobResp.setStatus(2)
        jobResp.setMsg(ExceptionUtils.getRootCauseMessage(exception))
    }
    jobResp
  }

    @Receiver
    override def batchChange(jobReqUpdate: JobReqBatchUpdate): util.ArrayList[JobRespProtocol] = {
      val jobReqList = jobReqUpdate.jobReq
      val jobRespList = new util.ArrayList[JobRespProtocol]()
      if(jobReqList != null){
        jobReqList.foreach(jobReq =>{
          jobReq.setExecutionCode(null)
          info("Update data to the database(往数据库中更新数据)：status:" + jobReq.getStatus )
          val jobResp = new JobRespProtocol
          Utils.tryCatch {
            if (jobReq.getErrorDesc != null) {
              if (jobReq.getErrorDesc.length > 256) {
                info(s"errorDesc is too long,we will cut some message")
                jobReq.setErrorDesc(jobReq.getErrorDesc.substring(0, 256))
                info(s"${jobReq.getErrorDesc}")
              }
            }
            if (jobReq.getStatus != null) {
              val oldStatus: String = jobHistoryMapper.selectJobHistoryStatusForUpdate(jobReq.getId)
              if (oldStatus != null && !shouldUpdate(oldStatus, jobReq.getStatus))
                throw new QueryException(120001,s"jobId:${jobReq.getId}，在数据库中的task状态为：${oldStatus}，更新的task状态为：${jobReq.getStatus}，更新失败！")
            }
            val jobUpdate = jobRequest2JobHistory(jobReq)
            jobUpdate.setUpdatedTime(new Timestamp(System.currentTimeMillis()))
            jobHistoryMapper.updateJobHistory(jobUpdate)

            // todo
            /*//to write cache
            if (TaskStatus.Succeed.toString.equals(jobReq.getStatus) && queryCacheService.needCache(jobReq)) {
              info("Write cache for task: " + jobReq.getId)
              jobReq.setExecutionCode(executionCode)
              queryCacheService.writeCache(jobReq)
            }*/

            val map = new util.HashMap[String, Object]
            map.put(JobRequestConstants.JOB_ID, jobReq.getId.asInstanceOf[Object])
            jobResp.setStatus(0)
            jobResp.setData(map)
          } {
            case e: Exception =>
              logger.error(s"Failed to update JobReqUpdate ${jobReq.getId},status ${jobReq.getStatus}", e)
              jobResp.setStatus(1)
              jobResp.setMsg(ExceptionUtils.getRootCauseMessage(e))
          }
          jobRespList.add(jobResp)
        })
      }
      jobRespList
    }

  @Receiver
  override def query(jobReqQuery: JobReqQuery): JobRespProtocol = {
    info("查询历史task：" + jobReqQuery.toString)
    val jobResp = new JobRespProtocol
    Utils.tryCatch {
      val jobHistory = jobRequest2JobHistory(jobReqQuery.jobReq)
      val task = jobHistoryMapper.selectJobHistory(jobHistory)
      val tasksWithDetails = new util.ArrayList[JobRequestWithDetail]
      task.asScala.foreach(job => {
        val subJobDetails = new util.ArrayList[SubJobDetail]()
        jobDetailMapper.selectJobDetailByJobHistoryId(job.getId).asScala.foreach(job => subJobDetails.add(jobdetail2SubjobDetail(job)))
        tasksWithDetails.add(new JobRequestWithDetail(jobHistory2JobRequest(job)).setSubJobDetailList(subJobDetails))
      })
      val map = new util.HashMap[String, Object]()
      map.put(JobRequestConstants.JOB_HISTORY_LIST, tasksWithDetails)
      jobResp.setStatus(0)
      jobResp.setData(map)
    } {
      case e: Exception =>
        logger.error(s"Failed to query job ${jobReqQuery.jobReq.getId}", e)
        jobResp.setStatus(1)
        jobResp.setMsg(ExceptionUtils.getRootCauseMessage(e))
    }
    jobResp
  }

  /*private def queryTaskList2RequestPersistTaskList(queryTask: java.util.List[QueryTask]): java.util.List[RequestPersistTask] = {
    import scala.collection.JavaConversions._
    val tasks = new util.ArrayList[RequestPersistTask]
    import org.apache.linkis.jobhistory.conversions.TaskConversions.queryTask2RequestPersistTask
    queryTask.foreach(f => tasks.add(f))
    tasks
  }*/

  override def getJobHistoryByIdAndName(jobId: java.lang.Long, userName: String): JobHistory = {
    val jobReq = new JobHistory
    jobReq.setId(jobId)
    jobReq.setSubmitUser(userName)
    val jobHistoryList = jobHistoryMapper.selectJobHistory(jobReq)
    if (jobHistoryList.isEmpty) null else jobHistoryList.get(0)
  }

  override def search(jobId: java.lang.Long, username: String, status: String, creator: String, sDate: Date, eDate: Date, engineType: String): util.List[JobHistory] = {
    import scala.collection.JavaConversions._
    val split: util.List[String] = if (status != null) status.split(",").toList else null
    val result = jobHistoryMapper.search(jobId, username, split, sDate, eDate, engineType)
    if (creator == null || result == null | result.size() == 0) {
      result
    } else {
      /* filter result given creator, creator is stored as a label in db, so we cannot filter directly by SQL */
      filterJobHistoryByCreator(result, creator)
    }
  }

  private def filterJobHistoryByCreator(jobHistoryList: util.List[JobHistory], creator: String): util.List[JobHistory] = {
    jobHistoryList.filter(jobHistory => {
      val labels = TaskConversions.getLabelListFromJson(jobHistory.getLabels)
      if (labels == null || labels.size() == 0) {
        warn("label is null or size is 0 for jobHistory record: " + jobHistory.getId)
        false
      } else {
        val userCreatorLabel = LabelUtil.getUserCreatorLabel(labels)
        if (userCreatorLabel == null) {
          false
        } else {
          StringUtils.equals(userCreatorLabel.getCreator, creator)
        }
      }
    })
  }

  override def getQueryVOList(list: java.util.List[JobHistory]): java.util.List[JobRequest] = {
    jobHistory2JobRequest(list)
  }

  private def shouldUpdate(oldStatus: String, newStatus: String): Boolean =  {
    if(TaskStatus.valueOf(oldStatus) == TaskStatus.valueOf(newStatus)){
      true
    }else{
      TaskStatus.valueOf(oldStatus).ordinal <= TaskStatus.valueOf(newStatus).ordinal && !TaskStatus.isComplete(TaskStatus.valueOf(oldStatus))
    }
  }


  override def searchOne(jobId: lang.Long, sDate: Date, eDate: Date): JobHistory = {
    Iterables.getFirst(
      jobHistoryMapper.search(jobId, null, null, sDate, eDate, null),
      {
        val queryJobHistory = new QueryJobHistory
        queryJobHistory.setId(jobId)
        queryJobHistory.setStatus(TaskStatus.Inited.toString)
        queryJobHistory.setSubmitUser("EMPTY")
        queryJobHistory
      })
  }

}

