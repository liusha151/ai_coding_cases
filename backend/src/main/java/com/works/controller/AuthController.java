package com.works.controller;

import com.works.common.Result;
import com.works.model.LoginDTO;
import com.works.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "认证管理")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto.getUsername(), dto.getPassword()));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户登出")
    public Result<?> logout() {
        return Result.success(null);
    }
}
