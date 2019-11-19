package com.webank.wedatasphere.linkis.bml.vo;

import java.util.Date;

/**
 * created by cooperyang on 2019/5/30
 * Description: 给用户查看的资源基本信息的VO类
 */

public class ResourceBasicVO {
    private String resourceId;
    private String owner;
    private String downloadedFileName;
    private String system;
    private String expireTime;
    private Integer numberOfVerions;
    private Date createTime;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDownloadedFileName() {
        return downloadedFileName;
    }

    public void setDownloadedFileName(String downloadedFileName) {
        this.downloadedFileName = downloadedFileName;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getNumberOfVerions() {
        return numberOfVerions;
    }

    public void setNumberOfVerions(int numberOfVerions) {
        this.numberOfVerions = numberOfVerions;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
