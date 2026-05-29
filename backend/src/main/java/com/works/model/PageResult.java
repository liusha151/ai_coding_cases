package com.works.model;

import java.util.List;

public class PageResult<T> {
    private List<T> list;
    private int total;
    private int page;
    private int size;

    public PageResult(List<T> list, int total, int page, int size) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<T> getList() { return list; }
    public int getTotal() { return total; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
