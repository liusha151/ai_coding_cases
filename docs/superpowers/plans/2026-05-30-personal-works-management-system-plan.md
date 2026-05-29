# Personal Works Management System Implementation Plan

> **For agentic workers:** Use subagent-driven-development (recommended) or executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a full-stack personal works information management system with works CRUD, statistics, system management, theme switching, and API gateway security.

**Architecture:** Spring Boot + Vue 2 + Element UI, RESTful API via gateway filter, JWT auth, MySQL 5.6.23 + MyBatis, ECharts for statistics, HTTPS + AES encryption.

**Tech Stack:** Java 8, Spring Boot 2.x, MyBatis, MySQL 5.6.23, Vue 2, Element UI, ECharts, JWT, AES

---

## File Structure

```
backend/
├── pom.xml
├── sql/init.sql
├── src/main/java/com/works/
│   ├── WorksApplication.java
│   ├── config/WebMvcConfig.java
│   ├── config/SecurityConfig.java
│   ├── filter/AuthFilter.java
│   ├── common/Result.java
│   ├── common/PageRequest.java
│   ├── common/Constants.java
│   ├── exception/GlobalExceptionHandler.java
│   ├── exception/BusinessException.java
│   ├── util/JwtUtil.java
│   ├── util/AesUtil.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── WorksController.java
│   │   ├── StatisticsController.java
│   │   ├── DictController.java
│   │   └── UserController.java
│   ├── service/
│   │   ├── WorksService.java
│   │   ├── StatisticsService.java
│   │   ├── DictService.java
│   │   ├── UserService.java
│   │   └── AuthService.java
│   ├── service/impl/
│   │   ├── WorksServiceImpl.java
│   │   ├── StatisticsServiceImpl.java
│   │   ├── DictServiceImpl.java
│   │   ├── UserServiceImpl.java
│   │   └── AuthServiceImpl.java
│   ├── mapper/
│   │   ├── WorksMapper.java
│   │   ├── DictMapper.java
│   │   └── UserMapper.java
│   └── model/
│       ├── Works.java
│       ├── DictType.java
│       ├── DictItem.java
│       ├── User.java
│       ├── WorksQueryDTO.java
│       ├── StatisticsQueryDTO.java
│       ├── LoginDTO.java
│       └── PageResult.java
├── src/main/resources/
│   ├── application.yml
│   └── mapper/
│       ├── WorksMapper.xml
│       ├── DictMapper.xml
│       └── UserMapper.xml
│
frontend/
├── package.json
├── vue.config.js
├── src/
│   ├── main.js
│   ├── App.vue
│   ├── router/index.js
│   ├── store/index.js
│   ├── api/
│   │   ├── works.js
│   │   ├── statistics.js
│   │   ├── dict.js
│   │   ├── auth.js
│   │   └── user.js
│   ├── utils/request.js
│   ├── utils/auth.js
│   ├── theme/tech.css
│   ├── theme/simple.css
│   ├── components/ThemeToggle.vue
│   ├── components/DictSelect.vue
│   ├── layouts/MainLayout.vue
│   ├── views/login/Login.vue
│   ├── views/works/WorksList.vue
│   ├── views/works/WorksForm.vue
│   ├── views/works/WorksDetail.vue
│   ├── views/statistics/Statistics.vue
│   ├── views/system/UserManagement.vue
│   └── views/system/DictManagement.vue
```

---

## Phase 1: Project Scaffolding

### Task 1: Database initialization

**Files:**
- Create: `backend/sql/init.sql`

- [ ] **Step 1: Write init.sql**

```sql
CREATE DATABASE IF NOT EXISTS works_management DEFAULT CHARSET utf8mb4;

USE works_management;

-- 数据字典类型表
CREATE TABLE dict_type (
  id INT PRIMARY KEY AUTO_INCREMENT,
  type_code VARCHAR(50) NOT NULL UNIQUE,
  type_name VARCHAR(100) NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据字典项表
CREATE TABLE dict_item (
  id INT PRIMARY KEY AUTO_INCREMENT,
  type_code VARCHAR(50) NOT NULL,
  item_value VARCHAR(100) NOT NULL,
  sort_order INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (type_code) REFERENCES dict_type(type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 著作信息表
CREATE TABLE works (
  id INT PRIMARY KEY AUTO_INCREMENT,
  work_no VARCHAR(50) NOT NULL COMMENT '工号',
  author_name VARCHAR(100) NOT NULL COMMENT '姓名',
  work_type VARCHAR(50) NOT NULL COMMENT '著作类型(字典)',
  work_name VARCHAR(200) NOT NULL COMMENT '名称',
  publish_path VARCHAR(500) COMMENT '发表路径',
  status VARCHAR(50) NOT NULL COMMENT '状态(字典)',
  personal_rank INT NOT NULL COMMENT '个人排名(>0)',
  co_authors VARCHAR(500) COMMENT '著作人',
  acquire_date DATE NOT NULL COMMENT '取得时间',
  remark TEXT COMMENT '备注',
  create_by VARCHAR(100),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 系统用户表
CREATE TABLE sys_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '管理员/普通用户',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始数据
INSERT INTO dict_type (type_code, type_name) VALUES
('work_type', '著作类型'),
('work_status', '状态');

INSERT INTO dict_item (type_code, item_value, sort_order) VALUES
('work_type', '科技论文', 1),
('work_type', '管理论文', 2),
('work_type', '发明专利', 3),
('work_type', '国防专利', 4),
('work_type', '实用新型专利', 5),
('work_status', '已提交', 1),
('work_status', '已审理', 2),
('work_status', '已公开', 3),
('work_status', '已授权', 4),
('work_status', '已驳回', 5),
('work_status', '已投稿', 6),
('work_status', '已录用', 7),
('work_status', '已发表', 8);

-- 默认管理员 admin/admin123
INSERT INTO sys_user (username, password, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin');
```

---

### Task 2: Backend Maven project setup

**Files:**
- Create: `backend/pom.xml`
- Create: `backend/src/main/resources/application.yml`

- [ ] **Step 1: Write pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.12.RELEASE</version>
    </parent>
    <groupId>com.works</groupId>
    <artifactId>works-management</artifactId>
    <version>1.0.0</version>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.4</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.49</version>
        </dependency>
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.18.2</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: Write application.yml**

```yaml
server:
  port: 8015
spring:
  datasource:
    url: jdbc:mysql://localhost:13306/works_management?useSSL=false&characterEncoding=utf8mb4
    username: root
    password: 123
    driver-class-name: com.mysql.jdbc.Driver
  jackson:
    date-format: yyyy-MM-dd
    time-zone: Asia/Shanghai
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.works.model
  configuration:
    map-underscore-to-camel-case: true
jwt:
  secret: WORKS_MGMT_SECRET_KEY_2026
  expiration: 86400000
```

- [ ] **Step 3: Write WorksApplication.java**

```java
package com.works;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorksApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorksApplication.class, args);
    }
}
```

---

### Task 3: Vue frontend project setup

**Files:**
- Create: `frontend/package.json`
- Create: `frontend/vue.config.js`

- [ ] **Step 1: Write package.json**

```json
{
  "name": "works-management-frontend",
  "version": "1.0.0",
  "scripts": {
    "dev": "vue-cli-service serve",
    "build": "vue-cli-service build"
  },
  "dependencies": {
    "vue": "^2.6.14",
    "vue-router": "^3.5.3",
    "vuex": "^3.6.2",
    "element-ui": "^2.15.6",
    "axios": "^0.24.0",
    "echarts": "^5.3.2"
  },
  "devDependencies": {
    "@vue/cli-service": "^4.5.15",
    "vue-template-compiler": "^2.6.14"
  }
}
```

- [ ] **Step 2: Write vue.config.js**

```js
module.exports = {
  devServer: {
    port: 8016,
    proxy: {
      '/api': {
        target: 'http://localhost:8015',
        changeOrigin: true
      }
    }
  }
}
```

---

## Phase 2: Backend Foundation

### Task 4: Common utilities and exception handling

**Files:**
- Create: `backend/src/main/java/com/works/common/Result.java`
- Create: `backend/src/main/java/com/works/common/PageRequest.java`
- Create: `backend/src/main/java/com/works/common/Constants.java`
- Create: `backend/src/main/java/com/works/exception/BusinessException.java`
- Create: `backend/src/main/java/com/works/exception/GlobalExceptionHandler.java`

- [ ] **Step 1: Write Result.java**

```java
package com.works.common;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() {}
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
```

- [ ] **Step 2: Write PageRequest.java**

```java
package com.works.common;

public class PageRequest {
    private int page = 1;
    private int size = 10;

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getOffset() { return (page - 1) * size; }
}
```

- [ ] **Step 3: Write Constants.java**

```java
package com.works.common;

public class Constants {
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
```

- [ ] **Step 4: Write BusinessException.java**

```java
package com.works.exception;

public class BusinessException extends RuntimeException {
    private int code;
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode() { return code; }
}
```

- [ ] **Step 5: Write GlobalExceptionHandler.java**

```java
package com.works.exception;

import com.works.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}
```

---

### Task 5: Security utilities (JWT + AES)

**Files:**
- Create: `backend/src/main/java/com/works/util/JwtUtil.java`
- Create: `backend/src/main/java/com/works/util/AesUtil.java`

- [ ] **Step 1: Write JwtUtil.java**

```java
package com.works.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "WORKS_MGMT_SECRET_KEY_2026";
    private static final long EXPIRATION = 86400000L;

    public static String generateToken(String username, String role) {
        return JWT.create()
                .withClaim("username", username)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    public static String getUsername(String token) {
        return verifyToken(token).getClaim("username").asString();
    }

    public static String getRole(String token) {
        return verifyToken(token).getClaim("role").asString();
    }
}
```

- [ ] **Step 2: Write AesUtil.java**

```java
package com.works.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesUtil {
    private static final String KEY = "WorksMgmt2026!!";

    public static String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    public static String decrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
    }
}
```

---

### Task 6: Auth filter and security config

**Files:**
- Create: `backend/src/main/java/com/works/filter/AuthFilter.java`
- Create: `backend/src/main/java/com/works/config/WebMvcConfig.java`
- Create: `backend/src/main/java/com/works/config/SecurityConfig.java`

- [ ] **Step 1: Write AuthFilter.java**

```java
package com.works.filter;

import com.works.common.Constants;
import com.works.util.JwtUtil;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();

        if (path.equals("/api/v1/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader(Constants.TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            res.setStatus(401);
            res.setContentType("application/json");
            res.getWriter().write("{\"code\":401,\"message\":\"未授权\"}");
            return;
        }

        try {
            String token = authHeader.substring(Constants.TOKEN_PREFIX.length());
            JwtUtil.verifyToken(token);
            req.setAttribute("username", JwtUtil.getUsername(token));
            req.setAttribute("role", JwtUtil.getRole(token));
            chain.doFilter(request, response);
        } catch (Exception e) {
            res.setStatus(401);
            res.setContentType("application/json");
            res.getWriter().write("{\"code\":401,\"message\":\"无效的令牌\"}");
        }
    }
}
```

- [ ] **Step 2: Write WebMvcConfig.java**

```java
package com.works.config;

import com.works.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8016")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new AuthFilter());
        bean.addUrlPatterns("/api/v1/*");
        bean.setOrder(1);
        return bean;
    }
}
```

- [ ] **Step 3: Write SecurityConfig.java**

```java
package com.works.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests().anyRequest().permitAll();
    }
}
```

---

## Phase 3: Backend Auth Module

### Task 7: Auth module (login)

**Files:**
- Create: `backend/src/main/java/com/works/model/LoginDTO.java`
- Create: `backend/src/main/java/com/works/service/AuthService.java`
- Create: `backend/src/main/java/com/works/service/impl/AuthServiceImpl.java`
- Create: `backend/src/main/java/com/works/controller/AuthController.java`

- [ ] **Step 1: Write LoginDTO.java**

```java
package com.works.model;

public class LoginDTO {
    private String username;
    private String password;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
```

- [ ] **Step 2: Write AuthService.java**

```java
package com.works.service;

import java.util.Map;

public interface AuthService {
    Map<String, Object> login(String username, String password);
}
```

- [ ] **Step 3: Write AuthServiceImpl.java**

```java
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

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

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
```

- [ ] **Step 4: Write AuthController.java**

```java
package com.works.controller;

import com.works.common.Result;
import com.works.model.LoginDTO;
import com.works.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto.getUsername(), dto.getPassword()));
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success(null);
    }
}
```

---

### Task 8: User entity and mapper

**Files:**
- Create: `backend/src/main/java/com/works/model/User.java`
- Create: `backend/src/main/java/com/works/mapper/UserMapper.java`
- Create: `backend/src/main/resources/mapper/UserMapper.xml`

- [ ] **Step 1: Write User.java**

```java
package com.works.model;

import java.util.Date;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String role;
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
```

- [ ] **Step 2: Write UserMapper.java**

```java
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
```

- [ ] **Step 3: Write UserMapper.xml**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.works.mapper.UserMapper">
    <resultMap id="userMap" type="User">
        <id column="id" property="id"/>
        <result column="username" property="username"/>
        <result column="password" property="password"/>
        <result column="role" property="role"/>
        <result column="create_time" property="createTime"/>
        <result column="update_time" property="updateTime"/>
    </resultMap>
    <select id="findByUsername" resultMap="userMap">
        SELECT * FROM sys_user WHERE username = #{username}
    </select>
    <select id="findById" resultMap="userMap">
        SELECT * FROM sys_user WHERE id = #{id}
    </select>
    <select id="findAll" resultMap="userMap">
        SELECT * FROM sys_user ORDER BY id
    </select>
    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO sys_user (username, password, role) VALUES (#{username}, #{password}, #{role})
    </insert>
    <update id="update">
        UPDATE sys_user SET username=#{username}, password=#{password}, role=#{role} WHERE id=#{id}
    </update>
    <delete id="deleteById">
        DELETE FROM sys_user WHERE id = #{id}
    </delete>
</mapper>
```

---

## Phase 4: Backend Works CRUD

### Task 9: Works entity and mapper

**Files:**
- Create: `backend/src/main/java/com/works/model/Works.java`
- Create: `backend/src/main/java/com/works/model/WorksQueryDTO.java`
- Create: `backend/src/main/java/com/works/mapper/WorksMapper.java`
- Create: `backend/src/main/resources/mapper/WorksMapper.xml`

- [ ] **Step 1: Write Works.java**

```java
package com.works.model;

import java.util.Date;

public class Works {
    private Integer id;
    private String workNo;
    private String authorName;
    private String workType;
    private String workName;
    private String publishPath;
    private String status;
    private Integer personalRank;
    private String coAuthors;
    private Date acquireDate;
    private String remark;
    private String createBy;
    private Date createTime;
    private Date updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getWorkNo() { return workNo; }
    public void setWorkNo(String workNo) { this.workNo = workNo; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    public String getWorkName() { return workName; }
    public void setWorkName(String workName) { this.workName = workName; }
    public String getPublishPath() { return publishPath; }
    public void setPublishPath(String publishPath) { this.publishPath = publishPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPersonalRank() { return personalRank; }
    public void setPersonalRank(Integer personalRank) { this.personalRank = personalRank; }
    public String getCoAuthors() { return coAuthors; }
    public void setCoAuthors(String coAuthors) { this.coAuthors = coAuthors; }
    public Date getAcquireDate() { return acquireDate; }
    public void setAcquireDate(Date acquireDate) { this.acquireDate = acquireDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
```

- [ ] **Step 2: Write WorksQueryDTO.java**

```java
package com.works.model;

public class WorksQueryDTO {
    private String authorName;
    private String workType;
    private String workName;
    private String status;
    private int page = 1;
    private int size = 10;

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    public String getWorkName() { return workName; }
    public void setWorkName(String workName) { this.workName = workName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getOffset() { return (page - 1) * size; }
}
```

- [ ] **Step 3: Write WorksMapper.java**

```java
package com.works.mapper;

import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import java.util.List;

public interface WorksMapper {
    List<Works> findByPage(WorksQueryDTO query);
    int count(WorksQueryDTO query);
    Works findById(Integer id);
    int insert(Works works);
    int update(Works works);
    int deleteById(Integer id);
    List<String> findAllAuthorNames();
}
```

- [ ] **Step 4: Write WorksMapper.xml**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.works.mapper.WorksMapper">
    <resultMap id="worksMap" type="Works">
        <id column="id" property="id"/>
        <result column="work_no" property="workNo"/>
        <result column="author_name" property="authorName"/>
        <result column="work_type" property="workType"/>
        <result column="work_name" property="workName"/>
        <result column="publish_path" property="publishPath"/>
        <result column="status" property="status"/>
        <result column="personal_rank" property="personalRank"/>
        <result column="co_authors" property="coAuthors"/>
        <result column="acquire_date" property="acquireDate"/>
        <result column="remark" property="remark"/>
        <result column="create_by" property="createBy"/>
        <result column="create_time" property="createTime"/>
        <result column="update_time" property="updateTime"/>
    </resultMap>
    <select id="findByPage" resultMap="worksMap">
        SELECT * FROM works
        <where>
            <if test="authorName != null and authorName != ''">AND author_name LIKE CONCAT('%',#{authorName},'%')</if>
            <if test="workType != null and workType != ''">AND work_type = #{workType}</if>
            <if test="workName != null and workName != ''">AND work_name LIKE CONCAT('%',#{workName},'%')</if>
            <if test="status != null and status != ''">AND status = #{status}</if>
        </where>
        ORDER BY create_time DESC LIMIT #{size} OFFSET #{offset}
    </select>
    <select id="count" resultType="int">
        SELECT COUNT(*) FROM works
        <where>
            <if test="authorName != null and authorName != ''">AND author_name LIKE CONCAT('%',#{authorName},'%')</if>
            <if test="workType != null and workType != ''">AND work_type = #{workType}</if>
            <if test="workName != null and workName != ''">AND work_name LIKE CONCAT('%',#{workName},'%')</if>
            <if test="status != null and status != ''">AND status = #{status}</if>
        </where>
    </select>
    <select id="findById" resultMap="worksMap">SELECT * FROM works WHERE id = #{id}</select>
    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO works (work_no, author_name, work_type, work_name, publish_path, status, personal_rank, co_authors, acquire_date, remark, create_by)
        VALUES (#{workNo}, #{authorName}, #{workType}, #{workName}, #{publishPath}, #{status}, #{personalRank}, #{coAuthors}, #{acquireDate}, #{remark}, #{createBy})
    </insert>
    <update id="update">
        UPDATE works SET work_no=#{workNo}, author_name=#{authorName}, work_type=#{workType}, work_name=#{workName},
        publish_path=#{publishPath}, status=#{status}, personal_rank=#{personalRank}, co_authors=#{coAuthors},
        acquire_date=#{acquireDate}, remark=#{remark} WHERE id=#{id}
    </update>
    <delete id="deleteById">DELETE FROM works WHERE id = #{id}</delete>
    <select id="findAllAuthorNames" resultType="string">SELECT DISTINCT author_name FROM works ORDER BY author_name</select>
</mapper>
```

---

### Task 10: Works service

**Files:**
- Create: `backend/src/main/java/com/works/service/WorksService.java`
- Create: `backend/src/main/java/com/works/service/impl/WorksServiceImpl.java`
- Create: `backend/src/main/java/com/works/model/PageResult.java`

- [ ] **Step 1: Write PageResult.java**

```java
package com.works.model;

import java.util.List;

public class PageResult<T> {
    private List<T> list;
    private int total;
    private int page;
    private int size;

    public PageResult(List<T> list, int total, int page, int size) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<T> getList() { return list; }
    public int getTotal() { return total; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
```

- [ ] **Step 2: Write WorksService.java**

```java
package com.works.service;

import com.works.model.PageResult;
import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import java.util.List;

public interface WorksService {
    PageResult<Works> findByPage(WorksQueryDTO query);
    Works findById(Integer id);
    int create(Works works);
    int update(Works works);
    int delete(Integer id);
    List<String> findAllAuthorNames();
}
```

- [ ] **Step 3: Write WorksServiceImpl.java**

```java
package com.works.service.impl;

import com.works.exception.BusinessException;
import com.works.mapper.WorksMapper;
import com.works.model.PageResult;
import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import com.works.service.WorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorksServiceImpl implements WorksService {
    @Autowired
    private WorksMapper worksMapper;

    @Override
    public PageResult<Works> findByPage(WorksQueryDTO query) {
        List<Works> list = worksMapper.findByPage(query);
        int total = worksMapper.count(query);
        return new PageResult<>(list, total, query.getPage(), query.getSize());
    }

    @Override
    public Works findById(Integer id) {
        Works works = worksMapper.findById(id);
        if (works == null) throw new BusinessException(404, "著作信息不存在");
        return works;
    }

    @Override
    public int create(Works works) {
        if (works.getPersonalRank() == null || works.getPersonalRank() <= 0) {
            throw new BusinessException(400, "个人排名必须大于0");
        }
        return worksMapper.insert(works);
    }

    @Override
    public int update(Works works) {
        if (works.getId() == null) throw new BusinessException(400, "ID不能为空");
        if (works.getPersonalRank() != null && works.getPersonalRank() <= 0) {
            throw new BusinessException(400, "个人排名必须大于0");
        }
        return worksMapper.update(works);
    }

    @Override
    public int delete(Integer id) {
        return worksMapper.deleteById(id);
    }

    @Override
    public List<String> findAllAuthorNames() {
        return worksMapper.findAllAuthorNames();
    }
}
```

---

### Task 11: Works controller

**Files:**
- Create: `backend/src/main/java/com/works/controller/WorksController.java`

- [ ] **Step 1: Write WorksController.java**

```java
package com.works.controller;

import com.works.common.Result;
import com.works.model.PageResult;
import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import com.works.service.WorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/works")
public class WorksController {
    @Autowired
    private WorksService worksService;

    @GetMapping
    public Result<PageResult<Works>> findByPage(WorksQueryDTO query) {
        return Result.success(worksService.findByPage(query));
    }

    @GetMapping("/{id}")
    public Result<Works> findById(@PathVariable Integer id) {
        return Result.success(worksService.findById(id));
    }

    @PostMapping
    public Result<Integer> create(@RequestBody Works works, HttpServletRequest request) {
        works.setCreateBy((String) request.getAttribute("username"));
        return Result.success(worksService.create(works));
    }

    @PutMapping("/{id}")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody Works works) {
        works.setId(id);
        return Result.success(worksService.update(works));
    }

    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Integer id) {
        return Result.success(worksService.delete(id));
    }

    @GetMapping("/author-names")
    public Result<List<String>> findAllAuthorNames() {
        return Result.success(worksService.findAllAuthorNames());
    }
}
```

---

## Phase 5: Backend Dict Module

### Task 12: Dict entities and mapper

**Files:**
- Create: `backend/src/main/java/com/works/model/DictType.java`
- Create: `backend/src/main/java/com/works/model/DictItem.java`
- Create: `backend/src/main/java/com/works/mapper/DictMapper.java`
- Create: `backend/src/main/resources/mapper/DictMapper.xml`

- [ ] **Step 1: Write DictType.java**

```java
package com.works.model;

import java.util.Date;
import java.util.List;

public class DictType {
    private Integer id;
    private String typeCode;
    private String typeName;
    private Date createTime;
    private Date updateTime;
    private List<DictItem> items;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public List<DictItem> getItems() { return items; }
    public void setItems(List<DictItem> items) { this.items = items; }
}
```

- [ ] **Step 2: Write DictItem.java**

```java
package com.works.model;

import java.util.Date;

public class DictItem {
    private Integer id;
    private String typeCode;
    private String itemValue;
    private Integer sortOrder;
    private Date createTime;
    private Date updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public String getItemValue() { return itemValue; }
    public void setItemValue(String itemValue) { this.itemValue = itemValue; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
```

- [ ] **Step 3: Write DictMapper.java**

```java
package com.works.mapper;

import com.works.model.DictItem;
import com.works.model.DictType;
import java.util.List;

public interface DictMapper {
    List<DictType> findAllTypes();
    DictType findTypeByCode(String typeCode);
    int insertType(DictType dictType);
    int updateType(DictType dictType);
    int deleteType(String typeCode);
    List<DictItem> findItemsByType(String typeCode);
    DictItem findItemById(Integer id);
    int insertItem(DictItem item);
    int updateItem(DictItem item);
    int deleteItem(Integer id);
}
```

- [ ] **Step 4: Write DictMapper.xml**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.works.mapper.DictMapper">
    <resultMap id="typeMap" type="DictType">
        <id column="id" property="id"/><result column="type_code" property="typeCode"/>
        <result column="type_name" property="typeName"/>
        <result column="create_time" property="createTime"/><result column="update_time" property="updateTime"/>
    </resultMap>
    <resultMap id="itemMap" type="DictItem">
        <id column="id" property="id"/><result column="type_code" property="typeCode"/>
        <result column="item_value" property="itemValue"/><result column="sort_order" property="sortOrder"/>
        <result column="create_time" property="createTime"/><result column="update_time" property="updateTime"/>
    </resultMap>
    <select id="findAllTypes" resultMap="typeMap">SELECT * FROM dict_type ORDER BY id</select>
    <select id="findTypeByCode" resultMap="typeMap">SELECT * FROM dict_type WHERE type_code = #{typeCode}</select>
    <insert id="insertType" useGeneratedKeys="true" keyProperty="id">INSERT INTO dict_type (type_code, type_name) VALUES (#{typeCode}, #{typeName})</insert>
    <update id="updateType">UPDATE dict_type SET type_code=#{typeCode}, type_name=#{typeName} WHERE id=#{id}</update>
    <delete id="deleteType">DELETE FROM dict_type WHERE type_code = #{typeCode}</delete>
    <select id="findItemsByType" resultMap="itemMap">SELECT * FROM dict_item WHERE type_code = #{typeCode} ORDER BY sort_order</select>
    <select id="findItemById" resultMap="itemMap">SELECT * FROM dict_item WHERE id = #{id}</select>
    <insert id="insertItem" useGeneratedKeys="true" keyProperty="id">INSERT INTO dict_item (type_code, item_value, sort_order) VALUES (#{typeCode}, #{itemValue}, #{sortOrder})</insert>
    <update id="updateItem">UPDATE dict_item SET item_value=#{itemValue}, sort_order=#{sortOrder} WHERE id=#{id}</update>
    <delete id="deleteItem">DELETE FROM dict_item WHERE id = #{id}</delete>
</mapper>
```

---

### Task 13: Dict service and controller

**Files:**
- Create: `backend/src/main/java/com/works/service/DictService.java`
- Create: `backend/src/main/java/com/works/service/impl/DictServiceImpl.java`
- Create: `backend/src/main/java/com/works/controller/DictController.java`

- [ ] **Step 1: Write DictService.java**

```java
package com.works.service;

import com.works.model.DictItem;
import com.works.model.DictType;
import java.util.List;

public interface DictService {
    List<DictType> findAllTypes();
    DictType findTypeByCode(String typeCode);
    int createType(DictType dictType);
    int updateType(DictType dictType);
    int deleteType(String typeCode);
    List<DictItem> findItemsByType(String typeCode);
    int createItem(DictItem item);
    int updateItem(DictItem item);
    int deleteItem(Integer id);
}
```

- [ ] **Step 2: Write DictServiceImpl.java**

```java
package com.works.service.impl;

import com.works.mapper.DictMapper;
import com.works.model.DictItem;
import com.works.model.DictType;
import com.works.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<DictType> findAllTypes() { return dictMapper.findAllTypes(); }

    @Override
    public DictType findTypeByCode(String typeCode) { return dictMapper.findTypeByCode(typeCode); }

    @Override
    public int createType(DictType dictType) { return dictMapper.insertType(dictType); }

    @Override
    public int updateType(DictType dictType) { return dictMapper.updateType(dictType); }

    @Override
    public int deleteType(String typeCode) { return dictMapper.deleteType(typeCode); }

    @Override
    public List<DictItem> findItemsByType(String typeCode) { return dictMapper.findItemsByType(typeCode); }

    @Override
    public int createItem(DictItem item) { return dictMapper.insertItem(item); }

    @Override
    public int updateItem(DictItem item) { return dictMapper.updateItem(item); }

    @Override
    public int deleteItem(Integer id) { return dictMapper.deleteItem(id); }
}
```

- [ ] **Step 3: Write DictController.java**

```java
package com.works.controller;

import com.works.common.Result;
import com.works.model.DictItem;
import com.works.model.DictType;
import com.works.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @GetMapping("/types")
    public Result<List<DictType>> findAllTypes() {
        return Result.success(dictService.findAllTypes());
    }

    @PostMapping("/types")
    public Result<Integer> createType(@RequestBody DictType dictType) {
        return Result.success(dictService.createType(dictType));
    }

    @PutMapping("/types/{id}")
    public Result<Integer> updateType(@PathVariable Integer id, @RequestBody DictType dictType) {
        dictType.setId(id);
        return Result.success(dictService.updateType(dictType));
    }

    @DeleteMapping("/types/{typeCode}")
    public Result<Integer> deleteType(@PathVariable String typeCode) {
        return Result.success(dictService.deleteType(typeCode));
    }

    @GetMapping("/items/{typeCode}")
    public Result<List<DictItem>> findItemsByType(@PathVariable String typeCode) {
        return Result.success(dictService.findItemsByType(typeCode));
    }

    @PostMapping("/items")
    public Result<Integer> createItem(@RequestBody DictItem item) {
        return Result.success(dictService.createItem(item));
    }

    @PutMapping("/items/{id}")
    public Result<Integer> updateItem(@PathVariable Integer id, @RequestBody DictItem item) {
        item.setId(id);
        return Result.success(dictService.updateItem(item));
    }

    @DeleteMapping("/items/{id}")
    public Result<Integer> deleteItem(@PathVariable Integer id) {
        return Result.success(dictService.deleteItem(id));
    }
}
```

---

## Phase 6: Backend Statistics Module

### Task 14: Statistics service and controller

**Files:**
- Create: `backend/src/main/java/com/works/model/StatisticsQueryDTO.java`
- Create: `backend/src/main/java/com/works/service/StatisticsService.java`
- Create: `backend/src/main/java/com/works/service/impl/StatisticsServiceImpl.java`
- Create: `backend/src/main/java/com/works/controller/StatisticsController.java`

- [ ] **Step 1: Write StatisticsQueryDTO.java**

```java
package com.works.model;

public class StatisticsQueryDTO {
    private String authorName;
    private String workType;

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
}
```

- [ ] **Step 2: Write StatisticsService.java**

```java
package com.works.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    List<Map<String, Object>> countByWorkType(String authorName, String workType);
    List<Map<String, Object>> countByStatus(String authorName, String workType);
    List<Map<String, Object>> countByRank(String authorName, String workType);
    List<Map<String, Object>> countByYear(String authorName, String workType);
}
```

- [ ] **Step 3: Write StatisticsServiceImpl.java**

```java
package com.works.service.impl;

import com.works.mapper.WorksMapper;
import com.works.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private WorksMapper worksMapper;

    @Override
    public List<Map<String, Object>> countByWorkType(String authorName, String workType) {
        return worksMapper.countByWorkType(authorName, workType);
    }

    @Override
    public List<Map<String, Object>> countByStatus(String authorName, String workType) {
        return worksMapper.countByStatus(authorName, workType);
    }

    @Override
    public List<Map<String, Object>> countByRank(String authorName, String workType) {
        return worksMapper.countByRank(authorName, workType);
    }

    @Override
    public List<Map<String, Object>> countByYear(String authorName, String workType) {
        return worksMapper.countByYear(authorName, workType);
    }
}
```

- [ ] **Step 4: Write StatisticsController.java**

```java
package com.works.controller;

import com.works.common.Result;
import com.works.model.StatisticsQueryDTO;
import com.works.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public Result<Map<String, List<Map<String, Object>>>> getStatistics(StatisticsQueryDTO query) {
        Map<String, List<Map<String, Object>>> data = new HashMap<>();
        data.put("byWorkType", statisticsService.countByWorkType(query.getAuthorName(), query.getWorkType()));
        data.put("byStatus", statisticsService.countByStatus(query.getAuthorName(), query.getWorkType()));
        data.put("byRank", statisticsService.countByRank(query.getAuthorName(), query.getWorkType()));
        data.put("byYear", statisticsService.countByYear(query.getAuthorName(), query.getWorkType()));
        return Result.success(data);
    }
}
```

- [ ] **Step 5: Add statistics queries to WorksMapper.xml**

Append to `WorksMapper.xml` before `</mapper>`:

```xml
<select id="countByWorkType" resultType="map">
    SELECT work_type AS name, COUNT(*) AS value FROM works
    <where>
        <if test="authorName != null and authorName != ''">AND author_name = #{authorName}</if>
        <if test="workType != null and workType != ''">AND work_type = #{workType}</if>
    </where>
    GROUP BY work_type
</select>
<select id="countByStatus" resultType="map">
    SELECT status AS name, COUNT(*) AS value FROM works
    <where>
        <if test="authorName != null and authorName != ''">AND author_name = #{authorName}</if>
        <if test="workType != null and workType != ''">AND work_type = #{workType}</if>
    </where>
    GROUP BY status
</select>
<select id="countByRank" resultType="map">
    SELECT CONCAT(FLOOR((personal_rank-1)/5)*5+1, '-', FLOOR((personal_rank-1)/5)*5+5) AS name, COUNT(*) AS value FROM works
    <where>
        <if test="authorName != null and authorName != ''">AND author_name = #{authorName}</if>
        <if test="workType != null and workType != ''">AND work_type = #{workType}</if>
    </where>
    GROUP BY FLOOR((personal_rank-1)/5)
    ORDER BY name
</select>
<select id="countByYear" resultType="map">
    SELECT YEAR(acquire_date) AS name, COUNT(*) AS value FROM works
    <where>
        <if test="authorName != null and authorName != ''">AND author_name = #{authorName}</if>
        <if test="workType != null and workType != ''">AND work_type = #{workType}</if>
    </where>
    GROUP BY YEAR(acquire_date) ORDER BY name
</select>
```

- [ ] **Step 6: Add methods to WorksMapper.java**

Add to `WorksMapper.java`:

```java
List<Map<String, Object>> countByWorkType(@Param("authorName") String authorName, @Param("workType") String workType);
List<Map<String, Object>> countByStatus(@Param("authorName") String authorName, @Param("workType") String workType);
List<Map<String, Object>> countByRank(@Param("authorName") String authorName, @Param("workType") String workType);
List<Map<String, Object>> countByYear(@Param("authorName") String authorName, @Param("workType") String workType);
```

---

## Phase 7: Backend User Management

### Task 15: User management service and controller

**Files:**
- Create: `backend/src/main/java/com/works/service/UserService.java`
- Create: `backend/src/main/java/com/works/service/impl/UserServiceImpl.java`
- Create: `backend/src/main/java/com/works/controller/UserController.java`

- [ ] **Step 1: Write UserService.java**

```java
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
```

- [ ] **Step 2: Write UserServiceImpl.java**

```java
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
```

- [ ] **Step 3: Write UserController.java**

```java
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
    public Result<List<User>> findAll() {
        return Result.success(userService.findAll());
    }

    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable Integer id) {
        return Result.success(userService.findById(id));
    }

    @PostMapping
    public Result<Integer> create(@RequestBody User user) {
        return Result.success(userService.create(user));
    }

    @PutMapping("/{id}")
    public Result<Integer> update(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return Result.success(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Integer id) {
        return Result.success(userService.delete(id));
    }
}
```

---

## Phase 8: Frontend Foundation

### Task 16: Frontend project scaffold

**Files:**
- Create: `frontend/src/main.js`
- Create: `frontend/src/App.vue`
- Create: `frontend/public/index.html`

- [ ] **Step 1: Write index.html**

```html
<!DOCTYPE html>
<html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>个人著作信息管理系统</title></head><body><div id="app"></div></body></html>
```

- [ ] **Step 2: Write main.js**

```js
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './theme/tech.css'

Vue.use(ElementUI)
Vue.config.productionTip = false

new Vue({ router, store, render: h => h(App) }).$mount('#app')
```

- [ ] **Step 3: Write App.vue**

```vue
<template>
  <div id="app">
    <router-view />
  </div>
</template>
<script>
export default { name: 'App' }
</script>
```

---

### Task 17: Router and store

**Files:**
- Create: `frontend/src/router/index.js`
- Create: `frontend/src/store/index.js`

- [ ] **Step 1: Write router/index.js**

```js
import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/login/Login.vue'
import MainLayout from '../layouts/MainLayout.vue'
import WorksList from '../views/works/WorksList.vue'
import Statistics from '../views/statistics/Statistics.vue'
import DictManagement from '../views/system/DictManagement.vue'
import UserManagement from '../views/system/UserManagement.vue'

Vue.use(Router)

const router = new Router({
  routes: [
    { path: '/login', component: Login },
    {
      path: '/', component: MainLayout, meta: { requiresAuth: true },
      children: [
        { path: 'works', component: WorksList },
        { path: 'statistics', component: Statistics },
        { path: 'system/dict', component: DictManagement, meta: { role: 'admin' } },
        { path: 'system/users', component: UserManagement, meta: { role: 'admin' } },
        { path: '', redirect: '/works' }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
```

- [ ] **Step 2: Write store/index.js**

```js
import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    username: localStorage.getItem('username') || '',
    role: localStorage.getItem('role') || '',
    theme: localStorage.getItem('theme') || 'tech'
  },
  mutations: {
    SET_TOKEN(state, { token, username, role }) {
      state.token = token; state.username = username; state.role = role
      localStorage.setItem('token', token)
      localStorage.setItem('username', username)
      localStorage.setItem('role', role)
    },
    LOGOUT(state) {
      state.token = ''; state.username = ''; state.role = ''
      localStorage.removeItem('token'); localStorage.removeItem('username'); localStorage.removeItem('role')
    },
    SET_THEME(state, theme) { state.theme = theme; localStorage.setItem('theme', theme) }
  }
})
```

---

### Task 18: Axios request util

**Files:**
- Create: `frontend/src/utils/request.js`
- Create: `frontend/src/utils/auth.js`

- [ ] **Step 1: Write request.js**

```js
import axios from 'axios'
import store from '../store'
import router from '../router'

const service = axios.create({ baseURL: '/api/v1', timeout: 15000 })

service.interceptors.request.use(config => {
  const token = store.state.token
  if (token) { config.headers['Authorization'] = 'Bearer ' + token }
  return config
})

service.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response && error.response.status === 401) {
      store.commit('LOGOUT'); router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default service
```

- [ ] **Step 2: Write auth.js**

```js
export function getToken() { return localStorage.getItem('token') }
export function getRole() { return localStorage.getItem('role') }
export function isAdmin() { return getRole() === 'admin' }
```

---

### Task 19: Theme CSS files

**Files:**
- Create: `frontend/src/theme/tech.css`
- Create: `frontend/src/theme/simple.css`

- [ ] **Step 1: Write tech.css (科技风)**

```css
/* 科技风主题 - 深蓝色调 + 发光效果 */
body { background: #0a1628; color: #e0e8f0; font-family: 'Microsoft YaHei', sans-serif; }
.app-header { background: linear-gradient(135deg, #0d2137, #1a3a5c); border-bottom: 1px solid #1e90ff; box-shadow: 0 2px 12px rgba(30,144,255,0.3); }
.app-header .header-title { color: #1e90ff; text-shadow: 0 0 8px rgba(30,144,255,0.5); }
.el-table { background: #0d2137; color: #c8d6e5; }
.el-table th { background: #1a3a5c; color: #1e90ff; }
.el-table--border td { border-color: #1a3a5c; }
.el-card { background: #0d2137; border: 1px solid #1a3a5c; }
.el-button--primary { background: #1e90ff; border-color: #1e90ff; }
.el-input__inner { background: #0a1628; color: #e0e8f0; border-color: #1a3a5c; }
```

- [ ] **Step 2: Write simple.css (简略风)**

```css
/* 简略风主题 - 白色背景 + 极简边框 */
body { background: #f5f5f5; color: #333; font-family: 'Microsoft YaHei', sans-serif; }
.app-header { background: #fff; border-bottom: 1px solid #e0e0e0; box-shadow: none; }
.app-header .header-title { color: #333; text-shadow: none; }
.el-table { background: #fff; color: #333; }
.el-table th { background: #fafafa; color: #333; }
.el-table--border td { border-color: #ebeef5; }
.el-card { background: #fff; border: 1px solid #ebeef5; }
.el-button--primary { background: #409eff; border-color: #409eff; }
.el-input__inner { background: #fff; color: #333; border-color: #dcdfe6; }
```

---

### Task 20: Theme toggle and dict select components

**Files:**
- Create: `frontend/src/components/ThemeToggle.vue`
- Create: `frontend/src/components/DictSelect.vue`

- [ ] **Step 1: Write ThemeToggle.vue**

```vue
<template>
  <el-select v-model="currentTheme" @change="switchTheme" size="small" style="width:120px">
    <el-option label="科技风" value="tech"></el-option>
    <el-option label="简略风" value="simple"></el-option>
  </el-select>
</template>
<script>
export default {
  computed: { currentTheme: { get() { return this.$store.state.theme }, set(v) {} } },
  methods: {
    switchTheme(theme) {
      this.$store.commit('SET_THEME', theme)
      const link = document.getElementById('theme-style')
      if (link) link.href = theme === 'tech' ? '/theme/tech.css' : '/theme/simple.css'
    }
  }
}
</script>
```

- [ ] **Step 2: Write DictSelect.vue**

```vue
<template>
  <el-select v-model="val" :placeholder="placeholder" :clearable="clearable" @change="$emit('input', $event)">
    <el-option v-for="item in items" :key="item.id" :label="item.itemValue" :value="item.itemValue"></el-option>
  </el-select>
</template>
<script>
import { getDictItems } from '../api/dict'
export default {
  props: { typeCode: String, value: String, placeholder: { type: String, default: '请选择' }, clearable: { type: Boolean, default: true } },
  data() { return { items: [], val: this.value } },
  watch: { value(v) { this.val = v } },
  created() { getDictItems(this.typeCode).then(res => { this.items = res.data || [] }) }
}
</script>
```

---

### Task 21: Main layout with sidebar

**Files:**
- Create: `frontend/src/layouts/MainLayout.vue`

- [ ] **Step 1: Write MainLayout.vue**

```vue
<template>
  <el-container style="min-height:100vh">
    <el-aside width="220px" style="background:#0d2137">
      <div class="aside-title" style="padding:20px;color:#1e90ff;font-size:18px;text-align:center">著作管理系统</div>
      <el-menu :default-active="$route.path" router background-color="#0d2137" text-color="#c8d6e5" active-text-color="#1e90ff">
        <el-menu-item index="/works"><i class="el-icon-document"></i>个人著作管理</el-menu-item>
        <el-menu-item index="/statistics"><i class="el-icon-data-analysis"></i>个人著作统计</el-menu-item>
        <el-submenu index="system" v-if="isAdmin">
          <template slot="title"><i class="el-icon-setting"></i>系统管理</template>
          <el-menu-item index="/system/dict">数据字典管理</el-menu-item>
          <el-menu-item index="/system/users">账户管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="app-header" style="display:flex;align-items:center;justify-content:space-between;padding:0 20px">
        <span class="header-title" style="font-size:16px">个人著作信息管理系统</span>
        <div style="display:flex;align-items:center;gap:12px">
          <ThemeToggle />
          <span>{{ username }}</span>
          <el-button type="text" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>
<script>
import ThemeToggle from '../components/ThemeToggle'
import { isAdmin } from '../utils/auth'
export default {
  components: { ThemeToggle },
  computed: { username() { return this.$store.state.username }, isAdmin() { return isAdmin() } },
  methods: {
    logout() {
      this.$store.commit('LOGOUT'); this.$router.push('/login')
    }
  }
}
</script>
```

---

## Phase 9: Frontend Auth

### Task 22: Login page

**Files:**
- Create: `frontend/src/views/login/Login.vue`
- Create: `frontend/src/api/auth.js`

- [ ] **Step 1: Write api/auth.js**

```js
import request from '../utils/request'
export function login(data) { return request({ url: '/auth/login', method: 'post', data }) }
export function logout() { return request({ url: '/auth/logout', method: 'post' }) }
```

- [ ] **Step 2: Write Login.vue**

```vue
<template>
  <div class="login-container" style="display:flex;justify-content:center;align-items:center;height:100vh;background:linear-gradient(135deg,#0a1628,#1a3a5c)">
    <el-card style="width:400px;padding:30px">
      <h2 style="text-align:center;margin-bottom:30px">个人著作信息管理系统</h2>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="username"><el-input v-model="form.username" placeholder="用户名" prefix-icon="el-icon-user"></el-input></el-form-item>
        <el-form-item prop="password"><el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin"></el-input></el-form-item>
        <el-form-item><el-button type="primary" style="width:100%" :loading="loading" @click="handleLogin">登 录</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { login } from '../../api/auth'
export default {
  data() {
    return {
      form: { username: '', password: '' },
      rules: { username: [{ required: true, message: '请输入用户名' }], password: [{ required: true, message: '请输入密码' }] },
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const res = await login(this.form)
          this.$store.commit('SET_TOKEN', { token: res.data.token, username: res.data.username, role: res.data.role })
          this.$router.push('/')
        } catch (e) { this.$message.error('登录失败') }
        finally { this.loading = false }
      })
    }
  }
}
</script>
```

---

## Phase 10: Frontend Works Management

### Task 23: Works API module

**Files:**
- Create: `frontend/src/api/works.js`
- Create: `frontend/src/api/dict.js`

- [ ] **Step 1: Write works.js**

```js
import request from '../utils/request'
export function getWorksList(params) { return request({ url: '/works', method: 'get', params }) }
export function getWorksDetail(id) { return request({ url: `/works/${id}`, method: 'get' }) }
export function createWorks(data) { return request({ url: '/works', method: 'post', data }) }
export function updateWorks(id, data) { return request({ url: `/works/${id}`, method: 'put', data }) }
export function deleteWorks(id) { return request({ url: `/works/${id}`, method: 'delete' }) }
export function getAuthorNames() { return request({ url: '/works/author-names', method: 'get' }) }
```

- [ ] **Step 2: Write dict.js**

```js
import request from '../utils/request'
export function getDictTypes() { return request({ url: '/dict/types', method: 'get' }) }
export function getDictItems(typeCode) { return request({ url: `/dict/items/${typeCode}`, method: 'get' }) }
export function createDictItem(data) { return request({ url: '/dict/items', method: 'post', data }) }
export function updateDictItem(id, data) { return request({ url: `/dict/items/${id}`, method: 'put', data }) }
export function deleteDictItem(id) { return request({ url: `/dict/items/${id}`, method: 'delete' }) }
```

---

### Task 24: Works list page

**Files:**
- Create: `frontend/src/views/works/WorksList.vue`

- [ ] **Step 1: Write WorksList.vue**

```vue
<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名"><el-input v-model="query.authorName" placeholder="姓名" style="width:150px"></el-input></el-form-item>
        <el-form-item label="著作类型">
          <dict-select type-code="work_type" v-model="query.workType"></dict-select>
        </el-form-item>
        <el-form-item label="名称"><el-input v-model="query.workName" placeholder="名称" style="width:150px"></el-input></el-form-item>
        <el-form-item label="状态">
          <dict-select type-code="work_status" v-model="query.status"></dict-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
          <el-button type="success" @click="showAdd">新增著作</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <el-table :data="list" border stripe @row-dblclick="showDetail">
        <el-table-column prop="workNo" label="工号" width="100"></el-table-column>
        <el-table-column prop="authorName" label="姓名" width="100"></el-table-column>
        <el-table-column prop="workType" label="著作类型" width="120"></el-table-column>
        <el-table-column prop="workName" label="名称" min-width="180"></el-table-column>
        <el-table-column prop="publishPath" label="发表路径" min-width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100"></el-table-column>
        <el-table-column prop="personalRank" label="个人排名" width="80"></el-table-column>
        <el-table-column prop="coAuthors" label="著作人" width="150"></el-table-column>
        <el-table-column prop="acquireDate" label="取得时间" width="110"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="primary" @click="showEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @current-change="handlePageChange" :current-page="page" :total="total" :page-size="size" layout="total,prev,pager,next" style="margin-top:16px;text-align:right"></el-pagination>
    </el-card>
    <works-form ref="worksForm" :visible.sync="formVisible" :mode="formMode" :row="currentRow" @saved="search"></works-form>
    <works-detail ref="worksDetail" :visible.sync="detailVisible" :row="currentRow"></works-detail>
  </div>
</template>
<script>
import { getWorksList, deleteWorks, getAuthorNames } from '../../api/works'
import DictSelect from '../../components/DictSelect'
import WorksForm from './WorksForm'
import WorksDetail from './WorksDetail'
export default {
  components: { DictSelect, WorksForm, WorksDetail },
  data() {
    return {
      query: { authorName: '', workType: '', workName: '', status: '' },
      list: [], page: 1, size: 10, total: 0,
      formVisible: false, formMode: 'add', currentRow: null,
      detailVisible: false
    }
  },
  created() { this.search() },
  methods: {
    async search() {
      this.page = 1
      const res = await getWorksList({ ...this.query, page: this.page, size: this.size })
      this.list = res.data.list; this.total = res.data.total
    },
    async handlePageChange(p) {
      this.page = p
      const res = await getWorksList({ ...this.query, page: this.page, size: this.size })
      this.list = res.data.list; this.total = res.data.total
    },
    reset() { this.query = { authorName: '', workType: '', workName: '', status: '' }; this.search() },
    showAdd() { this.formMode = 'add'; this.currentRow = null; this.formVisible = true },
    showEdit(row) { this.formMode = 'edit'; this.currentRow = row; this.formVisible = true },
    showDetail(row) { this.currentRow = row; this.detailVisible = true },
    async handleDelete(row) {
      await this.$confirm('确认删除该著作信息？', '提示', { type: 'warning' })
      await deleteWorks(row.id)
      this.$message.success('删除成功'); this.search()
    }
  }
}
</script>
```

---

### Task 25: Works form (add/edit)

**Files:**
- Create: `frontend/src/views/works/WorksForm.vue`

- [ ] **Step 1: Write WorksForm.vue**

```vue
<template>
  <el-dialog :title="mode==='add'?'新增著作':'编辑著作'" :visible.sync="visible" width="700px" @closed="handleClosed">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="12"><el-form-item label="工号" prop="workNo"><el-input v-model="form.workNo"></el-input></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="姓名" prop="authorName"><el-input v-model="form.authorName"></el-input></el-form-item></el-col>
      </el-row>
      <el-row>
        <el-col :span="12"><el-form-item label="著作类型" prop="workType">
          <dict-select type-code="work_type" v-model="form.workType"></dict-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="状态" prop="status">
          <dict-select type-code="work_status" v-model="form.status"></dict-select>
        </el-form-item></el-col>
      </el-row>
      <el-form-item label="名称" prop="workName"><el-input v-model="form.workName"></el-input></el-form-item>
      <el-form-item label="发表路径" prop="publishPath"><el-input v-model="form.publishPath"></el-input></el-form-item>
      <el-row>
        <el-col :span="12"><el-form-item label="个人排名" prop="personalRank"><el-input-number v-model="form.personalRank" :min="1"></el-input-number></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="取得时间" prop="acquireDate"><el-date-picker v-model="form.acquireDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker></el-form-item></el-col>
      </el-row>
      <el-form-item label="著作人" prop="coAuthors"><el-input v-model="form.coAuthors"></el-input></el-form-item>
      <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3"></el-input></el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="save">保存</el-button>
    </span>
  </el-dialog>
</template>
<script>
import DictSelect from '../../components/DictSelect'
import { createWorks, updateWorks } from '../../api/works'
export default {
  components: { DictSelect },
  props: { visible: Boolean, mode: String, row: Object },
  data() {
    return {
      form: { workNo: '', authorName: '', workType: '', workName: '', publishPath: '', status: '', personalRank: 1, coAuthors: '', acquireDate: '', remark: '' },
      rules: {
        workNo: [{ required: true, message: '请输入工号' }],
        authorName: [{ required: true, message: '请输入姓名' }],
        workType: [{ required: true, message: '请选择著作类型' }],
        workName: [{ required: true, message: '请输入名称' }],
        status: [{ required: true, message: '请选择状态' }],
        personalRank: [{ required: true, message: '请输入个人排名' }],
        acquireDate: [{ required: true, message: '请选择取得时间' }],
        coAuthors: [{ required: true, message: '请输入著作人' }]
      },
      saving: false
    }
  },
  watch: {
    row: { immediate: true, handler(n) { if (n && this.mode === 'edit') this.form = { ...n } else this.resetForm() } }
  },
  methods: {
    resetForm() { this.form = { workNo: '', authorName: '', workType: '', workName: '', publishPath: '', status: '', personalRank: 1, coAuthors: '', acquireDate: '', remark: '' } },
    handleClosed() { this.$refs.form.clearValidate(); this.resetForm() },
    async save() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.saving = true
        try {
          if (this.mode === 'add') await createWorks(this.form)
          else await updateWorks(this.row.id, this.form)
          this.$message.success('保存成功')
          this.$emit('update:visible', false)
          this.$emit('saved')
        } catch (e) { this.$message.error('保存失败') }
        finally { this.saving = false }
      })
    }
  }
}
</script>
```

---

### Task 26: Works detail view

**Files:**
- Create: `frontend/src/views/works/WorksDetail.vue`

- [ ] **Step 1: Write WorksDetail.vue**

```vue
<template>
  <el-dialog title="著作详情" :visible.sync="visible" width="600px">
    <el-descriptions :column="2" border v-if="row">
      <el-descriptions-item label="工号">{{ row.workNo }}</el-descriptions-item>
      <el-descriptions-item label="姓名">{{ row.authorName }}</el-descriptions-item>
      <el-descriptions-item label="著作类型">{{ row.workType }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ row.status }}</el-descriptions-item>
      <el-descriptions-item label="名称" :span="2">{{ row.workName }}</el-descriptions-item>
      <el-descriptions-item label="发表路径" :span="2">{{ row.publishPath }}</el-descriptions-item>
      <el-descriptions-item label="个人排名">{{ row.personalRank }}</el-descriptions-item>
      <el-descriptions-item label="取得时间">{{ row.acquireDate }}</el-descriptions-item>
      <el-descriptions-item label="著作人" :span="2">{{ row.coAuthors }}</el-descriptions-item>
      <el-descriptions-item label="备注" :span="2">{{ row.remark }}</el-descriptions-item>
    </el-descriptions>
  </el-dialog>
</template>
<script>
export default {
  props: { visible: Boolean, row: Object }
}
</script>
```

---

## Phase 11: Frontend Statistics

### Task 27: Statistics page with ECharts

**Files:**
- Create: `frontend/src/api/statistics.js`
- Create: `frontend/src/views/statistics/Statistics.vue`

- [ ] **Step 1: Write statistics.js**

```js
import request from '../utils/request'
export function getStatistics(params) { return request({ url: '/statistics', method: 'get', params }) }
```

- [ ] **Step 2: Write Statistics.vue**

```vue
<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名">
          <el-select v-model="query.authorName" allow-filterable clearable>
            <el-option v-for="n in authorNames" :key="n" :label="n" :value="n"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="著作类型">
          <dict-select type-code="work_type" v-model="query.workType"></dict-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadChart">查询</el-button>
          <el-button @click="query={authorName:'',workType:''};loadChart()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-row :gutter="16">
      <el-col :span="12" v-for="(item,idx) in charts" :key="idx" style="margin-bottom:16px">
        <el-card><div :ref="'chart'+idx" style="height:300px"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { getStatistics } from '../../api/statistics'
import { getAuthorNames } from '../../api/works'
import DictSelect from '../../components/DictSelect'
export default {
  components: { DictSelect },
  data() {
    return {
      query: { authorName: '', workType: '' },
      authorNames: [],
      charts: [
        { title: '按著作类型', key: 'byWorkType' },
        { title: '按状态', key: 'byStatus' },
        { title: '按排名', key: 'byRank' },
        { title: '按取得年份', key: 'byYear' }
      ]
    }
  },
  async created() {
    const res = await getAuthorNames()
    this.authorNames = res.data || []
    this.$nextTick(() => this.loadChart())
  },
  methods: {
    async loadChart() {
      const res = await getStatistics(this.query)
      const data = res.data
      this.charts.forEach((item, idx) => {
        const dom = this.$refs['chart' + idx]
        if (!dom || dom.length === 0) return
        const chart = echarts.init(dom[0])
        const list = data[item.key] || []
        chart.setOption({
          title: { text: item.title, left: 'center' },
          tooltip: { trigger: 'item' },
          xAxis: { type: 'category', data: list.map(i => i.name) },
          yAxis: { type: 'value' },
          series: [{ type: 'bar', data: list.map(i => i.value), itemStyle: { color: '#409eff' } }]
        })
      })
    }
  }
}
</script>
```

---

## Phase 12: Frontend System Management

### Task 28: User management page

**Files:**
- Create: `frontend/src/api/user.js`
- Create: `frontend/src/views/system/UserManagement.vue`

- [ ] **Step 1: Write user.js**

```js
import request from '../utils/request'
export function getUserList() { return request({ url: '/users', method: 'get' }) }
export function createUser(data) { return request({ url: '/users', method: 'post', data }) }
export function updateUser(id, data) { return request({ url: `/users/${id}`, method: 'put', data }) }
export function deleteUser(id) { return request({ url: `/users/${id}`, method: 'delete' }) }
```

- [ ] **Step 2: Write UserManagement.vue**

```vue
<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-button type="primary" @click="showAdd">新增账户</el-button>
    </el-card>
    <el-card>
      <el-table :data="list" border>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="role" label="角色" :formatter="r=>r.role==='admin'?'管理员':'普通用户'" width="120"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="showEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :title="mode==='add'?'新增账户':'编辑账户'" :visible.sync="dialogVisible" width="450px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username"></el-input></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" type="password" :placeholder="mode==='edit'?'留空不修改':'请输入密码'"></el-input></el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role"><el-option label="普通用户" value="user"></el-option><el-option label="管理员" value="admin"></el-option></el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getUserList, createUser, updateUser, deleteUser } from '../../api/user'
export default {
  data() {
    return {
      list: [], dialogVisible: false, mode: 'add', currentRow: null,
      form: { username: '', password: '', role: 'user' },
      rules: {
        username: [{ required: true, message: '请输入用户名' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色' }]
      }
    }
  },
  created() { this.loadList() },
  methods: {
    async loadList() { const res = await getUserList(); this.list = res.data || [] },
    showAdd() { this.mode = 'add'; this.currentRow = null; this.form = { username: '', password: '', role: 'user' }; this.dialogVisible = true },
    showEdit(row) { this.mode = 'edit'; this.currentRow = row; this.form = { username: row.username, password: '', role: row.role }; this.dialogVisible = true },
    async save() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        if (this.mode === 'add') await createUser(this.form)
        else await updateUser(this.currentRow.id, this.form)
        this.$message.success('保存成功'); this.dialogVisible = false; this.loadList()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该账户？', '提示', { type: 'warning' })
      await deleteUser(row.id); this.$message.success('删除成功'); this.loadList()
    }
  }
}
</script>
```

---

### Task 29: Dict management page

**Files:**
- Create: `frontend/src/views/system/DictManagement.vue`

- [ ] **Step 1: Write DictManagement.vue**

```vue
<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-button type="primary" @click="showAddItem">新增字典项</el-button>
    </el-card>
    <el-card>
      <el-tabs v-model="activeType">
        <el-tab-pane label="著作类型" name="work_type"></el-tab-pane>
        <el-tab-pane label="状态" name="work_status"></el-tab-pane>
      </el-tabs>
      <el-table :data="items" border>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="itemValue" label="字典值" width="200"></el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="showEditItem(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteItem(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :title="itemMode==='add'?'新增字典项':'编辑字典项'" :visible.sync="itemDialogVisible" width="400px">
      <el-form ref="itemForm" :model="itemForm" :rules="itemRules" label-width="80px">
        <el-form-item label="字典值" prop="itemValue"><el-input v-model="itemForm.itemValue"></el-input></el-form-item>
        <el-form-item label="排序" prop="sortOrder"><el-input-number v-model="itemForm.sortOrder" :min="0"></el-input-number></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="itemDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveItem">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getDictItems, createDictItem, updateDictItem, deleteDictItem } from '../../api/dict'
export default {
  data() {
    return {
      activeType: 'work_type', items: [],
      itemDialogVisible: false, itemMode: 'add', currentItem: null,
      itemForm: { itemValue: '', sortOrder: 0 },
      itemRules: { itemValue: [{ required: true, message: '请输入字典值' }] }
    }
  },
  watch: { activeType: { immediate: true, handler() { this.loadItems() } } },
  methods: {
    async loadItems() { const res = await getDictItems(this.activeType); this.items = res.data || [] },
    showAddItem() { this.itemMode = 'add'; this.currentItem = null; this.itemForm = { itemValue: '', sortOrder: 0 }; this.itemDialogVisible = true },
    showEditItem(row) { this.itemMode = 'edit'; this.currentItem = row; this.itemForm = { itemValue: row.itemValue, sortOrder: row.sortOrder }; this.itemDialogVisible = true },
    async saveItem() {
      this.$refs.itemForm.validate(async valid => {
        if (!valid) return
        const data = { ...this.itemForm, typeCode: this.activeType }
        if (this.itemMode === 'add') await createDictItem(data)
        else await updateDictItem(this.currentItem.id, data)
        this.$message.success('保存成功'); this.itemDialogVisible = false; this.loadItems()
      })
    },
    async handleDeleteItem(row) {
      await this.$confirm('确认删除该字典项？', '提示', { type: 'warning' })
      await deleteDictItem(row.id); this.$message.success('删除成功'); this.loadItems()
    }
  }
}
</script>
```

---

## Phase 13: Push to GitHub

### Task 30: Deploy and push

- [ ] **Step 1: Commit and push everything**

```bash
cd /path/to/project
git add .
git commit -m "feat: implement personal works management system"
git push origin master
```
