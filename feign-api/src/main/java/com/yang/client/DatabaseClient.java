package com.yang.client;

import com.yang.pojo.Doc;
import com.yang.pojo.Metadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * call the database service
 * @author YHR
 * @date 2022/8/14 56:21:04
 * @description
 */
@FeignClient("database-service")
public interface DatabaseClient {

    @GetMapping("/postgresql/createDoc")
    int createDocToDB(@RequestParam("metadata") Metadata metadata, @RequestParam("path") String path, @RequestParam("objectId") String objectId);

    @PutMapping("/postgresql/updateSolrDocId")
    int updateSolrDocId(@RequestParam("docId") int docId, @RequestParam("solrDocId") String solrDocId);

    @PostMapping("/query")
    List<Doc> queryDocByMetadata(@RequestBody Metadata metadata);

}
