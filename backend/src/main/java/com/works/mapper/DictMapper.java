package com.works.mapper;

import com.works.model.DictItem;
import com.works.model.DictType;
import java.util.List;

public interface DictMapper {
    List<DictType> findAllTypes();
    DictType findTypeByCode(String typeCode);
    int insertType(DictType dictType);
    int updateType(DictType dictType);
    int deleteType(String typeCode);
    List<DictItem> findItemsByType(String typeCode);
    DictItem findItemById(Integer id);
    int insertItem(DictItem item);
    int updateItem(DictItem item);
    int deleteItem(Integer id);
}
