package com.works.model;

import java.util.Date;

/** 系统用户实体，对应 sys_user 表，密码使用 BCrypt 加密存储 */
public class User {
    private Integer id;
    private String username; // 用户名
    private String password; // BCrypt 加密后的密码哈希
    private String role;     // 角色：admin（管理员）或 user（普通用户）
    private Date createTime;
    private Date updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
