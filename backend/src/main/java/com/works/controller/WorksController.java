package com.works.controller;

import com.works.common.Result;
import com.works.model.PageResult;
import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import com.works.service.WorksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/works")
@Api(tags = "著作管理")
public class WorksController {
    @Autowired
    private WorksService worksService;

    @GetMapping
    @ApiOperation(value = "分页查询著作列表")
    public Result<PageResult<Works>> findByPage(WorksQueryDTO query) {
        return Result.success(worksService.findByPage(query));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询著作详情")
    public Result<Works> findById(@PathVariable Integer id) {
        return Result.success(worksService.findById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增著作")
    public Result<Integer> create(@RequestBody Works works, HttpServletRequest request) {
        works.setCreateBy((String) request.getAttribute("username"));
        return Result.success(worksService.create(works));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改著作")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody Works works) {
        works.setId(id);
        return Result.success(worksService.update(works));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除著作")
    public Result<Integer> delete(@PathVariable Integer id) {
        return Result.success(worksService.delete(id));
    }

    @GetMapping("/author-names")
    @ApiOperation(value = "获取所有作者姓名列表")
    public Result<List<String>> findAllAuthorNames() {
        return Result.success(worksService.findAllAuthorNames());
    }
}
