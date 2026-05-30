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

/**
 * 著作管理控制器：提供著作信息的 CRUD 接口
 */
@RestController
@RequestMapping("/api/v1/works")
@Api(tags = "著作管理")
public class WorksController {
    @Autowired
    private WorksService worksService;

    /** 分页查询著作列表，支持按姓名、著作类型、名称、状态模糊过滤 */
    @GetMapping
    @ApiOperation(value = "分页查询著作列表")
    public Result<PageResult<Works>> findByPage(WorksQueryDTO query) {
        return Result.success(worksService.findByPage(query));
    }

    /** 根据 ID 查询单条著作详情 */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询著作详情")
    public Result<Works> findById(@PathVariable Integer id) {
        return Result.success(worksService.findById(id));
    }

    /** 新增著作：从 JWT Token 中提取创建人用户名 */
    @PostMapping
    @ApiOperation(value = "新增著作")
    public Result<Integer> create(@RequestBody Works works, HttpServletRequest request) {
        works.setCreateBy((String) request.getAttribute("username"));
        return Result.success(worksService.create(works));
    }

    /** 修改著作：根据 ID 更新著作信息 */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改著作")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody Works works) {
        works.setId(id);
        return Result.success(worksService.update(works));
    }

    /** 删除著作：根据 ID 物理删除 */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除著作")
    public Result<Integer> delete(@PathVariable Integer id) {
        return Result.success(worksService.delete(id));
    }

    /** 获取所有作者姓名列表（用于统计查询的下拉选择） */
    @GetMapping("/author-names")
    @ApiOperation(value = "获取所有作者姓名列表")
    public Result<List<String>> findAllAuthorNames() {
        return Result.success(worksService.findAllAuthorNames());
    }
}
