package com.works.model;

/** 统计查询参数 */
public class StatisticsQueryDTO {
    private String authorName;
    private String workType;

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
}
