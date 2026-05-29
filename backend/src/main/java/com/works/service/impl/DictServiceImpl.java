package com.works.service.impl;

import com.works.mapper.DictMapper;
import com.works.model.DictItem;
import com.works.model.DictType;
import com.works.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<DictType> findAllTypes() { return dictMapper.findAllTypes(); }
    @Override
    public DictType findTypeByCode(String typeCode) { return dictMapper.findTypeByCode(typeCode); }
    @Override
    public int createType(DictType dictType) { return dictMapper.insertType(dictType); }
    @Override
    public int updateType(DictType dictType) { return dictMapper.updateType(dictType); }
    @Override
    public int deleteType(String typeCode) { return dictMapper.deleteType(typeCode); }
    @Override
    public List<DictItem> findItemsByType(String typeCode) { return dictMapper.findItemsByType(typeCode); }
    @Override
    public int createItem(DictItem item) { return dictMapper.insertItem(item); }
    @Override
    public int updateItem(DictItem item) { return dictMapper.updateItem(item); }
    @Override
    public int deleteItem(Integer id) { return dictMapper.deleteItem(id); }
}
