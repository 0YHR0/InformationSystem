package com.yang.client;

import com.yang.pojo.Metadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * call the database service
 * @author YHR
 * @date 2022/8/14 56:21:04
 * @description
 */
@FeignClient("database-service")
public interface DatabaseClient {

    @GetMapping("/postgresql/createDoc/{metadata}/{path}/{ObjectId}")
    int createDocToDB(@PathVariable("metadata") Metadata matadata, @PathVariable("path") String path, @PathVariable("ObjectId")String ObjectId);

    @GetMapping("/postgresql/updateSolrDocId/{docId}/{solrDocId}")
    int updateSolrDocId(@PathVariable("docId") int docId, @PathVariable("solrDocId") String solrDocId);

}
