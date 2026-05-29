package com.works.model;

import java.util.Date;

public class Works {
    private Integer id;
    private String workNo;
    private String authorName;
    private String workType;
    private String workName;
    private String publishPath;
    private String status;
    private Integer personalRank;
    private String coAuthors;
    private Date acquireDate;
    private String remark;
    private String createBy;
    private Date createTime;
    private Date updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getWorkNo() { return workNo; }
    public void setWorkNo(String workNo) { this.workNo = workNo; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    public String getWorkName() { return workName; }
    public void setWorkName(String workName) { this.workName = workName; }
    public String getPublishPath() { return publishPath; }
    public void setPublishPath(String publishPath) { this.publishPath = publishPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPersonalRank() { return personalRank; }
    public void setPersonalRank(Integer personalRank) { this.personalRank = personalRank; }
    public String getCoAuthors() { return coAuthors; }
    public void setCoAuthors(String coAuthors) { this.coAuthors = coAuthors; }
    public Date getAcquireDate() { return acquireDate; }
    public void setAcquireDate(Date acquireDate) { this.acquireDate = acquireDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
