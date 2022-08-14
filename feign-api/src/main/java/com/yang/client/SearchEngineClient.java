package com.yang.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author YHR
 * @date 2022/8/14 44:22:54
 * @description
 */
@FeignClient("searchEngine-service")
public interface SearchEngineClient {

    @GetMapping("/index/{path}")
    String indexing(@PathVariable("path") String path);
}
