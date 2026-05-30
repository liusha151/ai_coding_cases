package com.works;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用启动入口：扫描 com.works.mapper 包下的 MyBatis Mapper 接口
 */
@SpringBootApplication
@MapperScan("com.works.mapper")
public class WorksApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorksApplication.class, args);
    }
}
