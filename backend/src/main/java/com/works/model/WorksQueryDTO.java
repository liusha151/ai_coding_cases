package com.works.model;

public class WorksQueryDTO {
    private String authorName;
    private String workType;
    private String workName;
    private String status;
    private int page = 1;
    private int size = 10;

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    public String getWorkName() { return workName; }
    public void setWorkName(String workName) { this.workName = workName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getOffset() { return (page - 1) * size; }
}
