package com.works.service.impl;

import com.works.mapper.UserMapper;
import com.works.model.User;
import com.works.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() { return userMapper.findAll(); }

    @Override
    public User findById(Integer id) { return userMapper.findById(id); }

    @Override
    public int create(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userMapper.insert(user);
    }

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
