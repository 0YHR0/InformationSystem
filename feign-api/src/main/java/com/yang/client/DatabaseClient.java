package com.yang.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pojo.Doc;
import pojo.Metadata;

import java.util.List;

/**
 * call the database service
 * @author YHR
 * @date 2022/8/14 56:21:04
 */
@FeignClient("database-service")
public interface DatabaseClient {

    @PostMapping("/postgresql/createDoc/{path}/{ObjectId}")
    int createDocToDB(@RequestBody Metadata matadata, @PathVariable("path") String path, @PathVariable("ObjectId")String ObjectId);

    @GetMapping("/postgresql/updateSolrDocId/{docId}/{solrDocId}")
    int updateSolrDocId(@PathVariable("docId") int docId, @PathVariable("solrDocId") String solrDocId);

    @GetMapping("/postgresql/queryByMetadata")
    List<Doc> queryDocByMetadata(@RequestParam("authorName") String authorName, @RequestParam("date") String date, @RequestParam("title")String title);

    @GetMapping("/postgresql/queryBySolrDocId")
    List<Doc> queryDocBySolrDocId(@RequestParam List<String> objectIds);

}
