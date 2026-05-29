package com.works.service;

import com.works.model.DictItem;
import com.works.model.DictType;
import java.util.List;

public interface DictService {
    List<DictType> findAllTypes();
    DictType findTypeByCode(String typeCode);
    int createType(DictType dictType);
    int updateType(DictType dictType);
    int deleteType(String typeCode);
    List<DictItem> findItemsByType(String typeCode);
    int createItem(DictItem item);
    int updateItem(DictItem item);
    int deleteItem(Integer id);
}
