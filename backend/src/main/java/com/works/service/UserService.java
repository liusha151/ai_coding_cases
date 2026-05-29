package com.works.service;

import com.works.model.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    int create(User user);
    int update(User user);
    int delete(Integer id);
}
