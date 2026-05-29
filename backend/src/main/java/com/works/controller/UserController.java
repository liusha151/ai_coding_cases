package com.works.controller;

import com.works.common.Result;
import com.works.model.User;
import com.works.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result<List<User>> findAll() { return Result.success(userService.findAll()); }

    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable Integer id) { return Result.success(userService.findById(id)); }

    @PostMapping
    public Result<Integer> create(@RequestBody User user) { return Result.success(userService.create(user)); }

    @PutMapping("/{id}")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id); return Result.success(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Integer id) { return Result.success(userService.delete(id)); }
}
