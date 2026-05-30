package com.works.common;

/** 分页请求参数基类，默认第1页每页10条 */
public class PageRequest {
    private int page = 1;
    private int size = 10;

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getOffset() { return (page - 1) * size; }
}
