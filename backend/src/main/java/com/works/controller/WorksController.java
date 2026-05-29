package com.works.controller;

import com.works.common.Result;
import com.works.model.PageResult;
import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import com.works.service.WorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/works")
public class WorksController {
    @Autowired
    private WorksService worksService;

    @GetMapping
    public Result<PageResult<Works>> findByPage(WorksQueryDTO query) {
        return Result.success(worksService.findByPage(query));
    }

    @GetMapping("/{id}")
    public Result<Works> findById(@PathVariable Integer id) {
        return Result.success(worksService.findById(id));
    }

    @PostMapping
    public Result<Integer> create(@RequestBody Works works, HttpServletRequest request) {
        works.setCreateBy((String) request.getAttribute("username"));
        return Result.success(worksService.create(works));
    }

    @PutMapping("/{id}")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody Works works) {
        works.setId(id);
        return Result.success(worksService.update(works));
    }

    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Integer id) {
        return Result.success(worksService.delete(id));
    }

    @GetMapping("/author-names")
    public Result<List<String>> findAllAuthorNames() {
        return Result.success(worksService.findAllAuthorNames());
    }
}
