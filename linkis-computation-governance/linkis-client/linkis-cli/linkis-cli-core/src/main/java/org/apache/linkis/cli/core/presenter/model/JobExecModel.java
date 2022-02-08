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

package org.apache.linkis.cli.core.presenter.model;

import org.apache.linkis.cli.common.entity.execution.jobexec.JobStatus;
import org.apache.linkis.cli.common.entity.job.OutputWay;

import java.util.Map;

/** @description: formulate data for presenter */
public abstract class JobExecModel implements PresenterModel {
    Map<String, String> extraMsg = null;
    /** ID for client itself */
    private String cid;
    /** ID generated by server */
    private String jobID;

    private JobStatus jobStatus = JobStatus.UNSUBMITTED;

    private OutputWay outputWay;
    private String outputPath;

    public final String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public final String getJobID() {
        return this.jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public final Map<String, String> getExtraMsg() {
        return this.extraMsg;
    }

    public final void setExtraMsg(Map<String, String> extraMsg) {
        this.extraMsg = extraMsg;
    }

    public final JobStatus getJobStatus() {
        return jobStatus;
    }

    public final void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public OutputWay getOutputWay() {
        return outputWay;
    }

    public void setOutputWay(OutputWay outputWay) {
        this.outputWay = outputWay;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public final boolean isJobSubmitted() {
        return !(this.getJobStatus() == JobStatus.UNSUBMITTED
                || this.getJobStatus() == JobStatus.SUBMITTING);
    }

    public final boolean isJobCompleted() {
        return this.isJobSuccess()
                || this.isJobFailure()
                || this.isJobCancelled()
                || this.isJobTimeout()
                || this.isJobAbnormalStatus();
    }

    public final boolean isJobSuccess() {
        return this.getJobStatus() == JobStatus.SUCCEED;
    }

    public final boolean isJobFailure() {
        return this.getJobStatus() == JobStatus.FAILED;
    }

    public final boolean isJobCancelled() {
        return this.getJobStatus() == JobStatus.CANCELLED;
    }

    public final boolean isJobTimeout() {
        return this.getJobStatus() == JobStatus.TIMEOUT;
    }

    public final boolean isJobAbnormalStatus() {
        return this.getJobStatus() == JobStatus.UNKNOWN;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
