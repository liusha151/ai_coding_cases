package com.works.service.impl;

import com.works.mapper.UserMapper;
import com.works.model.User;
import com.works.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 用户业务实现：新增时使用 BCrypt 加密密码，修改时可选更新密码
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() { return userMapper.findAll(); }

    @Override
    public User findById(Integer id) { return userMapper.findById(id); }

    /** 新增用户：自动对明文密码进行 BCrypt 加盐哈希 */
    @Override
    public int create(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userMapper.insert(user);
    }

    /** 修改用户：仅当密码字段非空时才重新加密 */
    @Override
    public int update(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        return userMapper.update(user);
    }

    @Override
    public int delete(Integer id) { return userMapper.deleteById(id); }
}
