package com.works.model;

import java.util.Date;
import java.util.List;

public class DictType {
    private Integer id;
    private String typeCode;
    private String typeName;
    private Date createTime;
    private Date updateTime;
    private List<DictItem> items;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public List<DictItem> getItems() { return items; }
    public void setItems(List<DictItem> items) { this.items = items; }
}
