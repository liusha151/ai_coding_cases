package com.works.mapper;

import com.works.model.User;
import java.util.List;

/**
 * 用户 Mapper：系统账户的数据库操作
 */
public interface UserMapper {
    User findByUsername(String username);
    User findById(Integer id);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int deleteById(Integer id);
}
