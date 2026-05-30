package com.works.controller;

import com.works.common.Result;
import com.works.model.User;
import com.works.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 用户管理控制器：系统账户的 CRUD，仅 admin 角色可操作
 */
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

    /** 新增用户：密码会自动进行 BCrypt 加密存储 */
    @PostMapping
    @ApiOperation(value = "新增用户")
    public Result<Integer> create(@RequestBody User user) { return Result.success(userService.create(user)); }

    /** 修改用户：仅当传入非空密码时才会重新加密 */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改用户")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id); return Result.success(userService.update(user));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户")
    public Result<Integer> delete(@PathVariable Integer id) { return Result.success(userService.delete(id)); }
}
