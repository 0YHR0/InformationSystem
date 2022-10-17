package com.yang.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pojo.Doc;
import pojo.SolrMetadata;

import java.util.List;

/**
 * call the database service
 * @author YHR
 * @date 2022/8/14 44:22:54
 * @description
 */
@FeignClient("searchEngine-service")
public interface SearchEngineClient {

    @GetMapping("/solr/index/{path}/{objectId}")
    String indexing(@PathVariable("path") String path, @PathVariable("objectId") String objectId);

    @GetMapping("/solr/query/{keywords}")
    List<SolrMetadata> querySolr(@PathVariable("keywords") String keywords);
}
