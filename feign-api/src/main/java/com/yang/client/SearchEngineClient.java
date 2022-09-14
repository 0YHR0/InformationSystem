package com.yang.client;

import com.yang.pojo.Doc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author YHR
 * @date 2022/8/14 44:22:54
 * @description
 */
@FeignClient("searchEngine-service")
public interface SearchEngineClient {

    @PostMapping("/index")
    String indexing(@RequestParam("path") String path, @RequestParam("objectId") String objectId);

    @GetMapping("/query/{keywords}")
    List<Doc> querySolr(@PathVariable("keywords") String keywords);
}
