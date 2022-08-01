package com.yang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SearchingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchingApplication.class, args);
    }

}
