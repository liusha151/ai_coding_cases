package com.works.controller;

import com.works.common.Result;
import com.works.model.DictItem;
import com.works.model.DictType;
import com.works.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 数据字典管理控制器：维护字典类型及字典项的增删改查
 */
@RestController
@RequestMapping("/api/v1/dict")
@Api(tags = "数据字典管理")
public class DictController {
    @Autowired
    private DictService dictService;

    @GetMapping("/types")
    @ApiOperation(value = "查询所有字典类型")
    public Result<List<DictType>> findAllTypes() { return Result.success(dictService.findAllTypes()); }

    @PostMapping("/types")
    @ApiOperation(value = "新增字典类型")
    public Result<Integer> createType(@RequestBody DictType dictType) { return Result.success(dictService.createType(dictType)); }

    @PutMapping("/types/{id}")
    @ApiOperation(value = "修改字典类型")
    public Result<Integer> updateType(@PathVariable Integer id, @RequestBody DictType dictType) {
        dictType.setId(id); return Result.success(dictService.updateType(dictType));
    }

    @DeleteMapping("/types/{typeCode}")
    @ApiOperation(value = "删除字典类型")
    public Result<Integer> deleteType(@PathVariable String typeCode) { return Result.success(dictService.deleteType(typeCode)); }

    /** 根据类型编码获取该类型下的所有字典项 */
    @GetMapping("/items/{typeCode}")
    @ApiOperation(value = "查询字典项列表")
    public Result<List<DictItem>> findItemsByType(@PathVariable String typeCode) { return Result.success(dictService.findItemsByType(typeCode)); }

    @PostMapping("/items")
    @ApiOperation(value = "新增字典项")
    public Result<Integer> createItem(@RequestBody DictItem item) { return Result.success(dictService.createItem(item)); }

    @PutMapping("/items/{id}")
    @ApiOperation(value = "修改字典项")
    public Result<Integer> updateItem(@PathVariable Integer id, @RequestBody DictItem item) {
        item.setId(id); return Result.success(dictService.updateItem(item));
    }

    @DeleteMapping("/items/{id}")
    @ApiOperation(value = "删除字典项")
    public Result<Integer> deleteItem(@PathVariable Integer id) { return Result.success(dictService.deleteItem(id)); }
}
