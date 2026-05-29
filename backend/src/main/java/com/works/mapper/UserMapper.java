package com.works.mapper;

import com.works.model.User;
import java.util.List;

public interface UserMapper {
    User findByUsername(String username);
    User findById(Integer id);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int deleteById(Integer id);
}
