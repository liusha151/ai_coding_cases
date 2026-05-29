package com.works.controller;

import com.works.common.Result;
import com.works.model.DictItem;
import com.works.model.DictType;
import com.works.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @GetMapping("/types")
    public Result<List<DictType>> findAllTypes() { return Result.success(dictService.findAllTypes()); }

    @PostMapping("/types")
    public Result<Integer> createType(@RequestBody DictType dictType) { return Result.success(dictService.createType(dictType)); }

    @PutMapping("/types/{id}")
    public Result<Integer> updateType(@PathVariable Integer id, @RequestBody DictType dictType) {
        dictType.setId(id); return Result.success(dictService.updateType(dictType));
    }

    @DeleteMapping("/types/{typeCode}")
    public Result<Integer> deleteType(@PathVariable String typeCode) { return Result.success(dictService.deleteType(typeCode)); }

    @GetMapping("/items/{typeCode}")
    public Result<List<DictItem>> findItemsByType(@PathVariable String typeCode) { return Result.success(dictService.findItemsByType(typeCode)); }

    @PostMapping("/items")
    public Result<Integer> createItem(@RequestBody DictItem item) { return Result.success(dictService.createItem(item)); }

    @PutMapping("/items/{id}")
    public Result<Integer> updateItem(@PathVariable Integer id, @RequestBody DictItem item) {
        item.setId(id); return Result.success(dictService.updateItem(item));
    }

    @DeleteMapping("/items/{id}")
    public Result<Integer> deleteItem(@PathVariable Integer id) { return Result.success(dictService.deleteItem(id)); }
}
