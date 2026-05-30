package com.works.model;

import java.util.Date;

/** 著作信息实体，对应数据库 works 表 */
public class Works {
    private Integer id;
    private String workNo;        // 工号
    private String authorName;    // 姓名
    private String workType;      // 著作类型（关联 dict_type=work_type）
    private String workName;      // 名称
    private String publishPath;   // 发表路径
    private String status;        // 状态（关联 dict_type=work_status）
    private Integer personalRank; // 个人排名
    private String coAuthors;     // 著作人
    private Date acquireDate;     // 取得时间
    private String remark;        // 备注
    private String createBy;      // 创建人
    private Date createTime;      // 创建时间
    private Date updateTime;      // 更新时间

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
