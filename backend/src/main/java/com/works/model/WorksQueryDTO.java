package com.works.model;

import com.works.common.PageRequest;

/** 著作分页查询参数，继承 PageRequest 获取分页信息 */
public class WorksQueryDTO extends PageRequest {
    private String authorName;
    private String workType;
    private String workName;
    private String status;

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    public String getWorkName() { return workName; }
    public void setWorkName(String workName) { this.workName = workName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
