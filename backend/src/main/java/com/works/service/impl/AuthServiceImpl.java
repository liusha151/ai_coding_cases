package com.works.service.impl;

import com.works.exception.BusinessException;
import com.works.mapper.UserMapper;
import com.works.model.User;
import com.works.service.AuthService;
import com.works.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证业务实现：基于 BCrypt 验证密码，登录成功返回 JWT Token
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    /** 用户登录：查询用户 → BCrypt 校验密码 → 生成 JWT 并返回 */
    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }
}
