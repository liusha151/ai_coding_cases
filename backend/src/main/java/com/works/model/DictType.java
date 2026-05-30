package com.works.model;

import java.util.Date;
import java.util.List;

/** 字典类型实体，对应 dict_type 表，如 work_type（著作类型）、work_status（状态） */
public class DictType {
    private Integer id;
    private String typeCode;   // 类型编码（如 work_type）
    private String typeName;   // 类型名称（如 "著作类型"）
    private Date createTime;
    private Date updateTime;
    private List<DictItem> items; // 该类型下的字典项列表

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
