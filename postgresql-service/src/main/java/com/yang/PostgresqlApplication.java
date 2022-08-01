package com.yang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Entry
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:29:23
 */
@MapperScan("com.yang.mapper")
@SpringBootApplication
public class PostgresqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostgresqlApplication.class, args);
    }
}
