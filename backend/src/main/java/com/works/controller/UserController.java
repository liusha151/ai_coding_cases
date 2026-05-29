package com.works.controller;

import com.works.common.Result;
import com.works.model.User;
import com.works.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "查询所有用户")
    public Result<List<User>> findAll() { return Result.success(userService.findAll()); }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询用户详情")
    public Result<User> findById(@PathVariable Integer id) { return Result.success(userService.findById(id)); }

    @PostMapping
    @ApiOperation(value = "新增用户")
    public Result<Integer> create(@RequestBody User user) { return Result.success(userService.create(user)); }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改用户")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id); return Result.success(userService.update(user));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户")
    public Result<Integer> delete(@PathVariable Integer id) { return Result.success(userService.delete(id)); }
}
