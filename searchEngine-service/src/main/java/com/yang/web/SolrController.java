package com.yang.web;

import com.yang.pojo.Doc;
import com.yang.servcie.DocService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * get the request which related to operate the solr cloud
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:29:50
 */
@RestController
@RequestMapping("solr")
public class SolrController {

    @Autowired
    private DocService docService;

    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name --- from solr-service";
    }

    /**
     * Get the article by keywords
     * @param keywords
     * @return
     */
    @GetMapping("/query/{keywords}")
    public List<Doc> querySolr(@PathVariable("keywords") String keywords){
       return docService.querySolr(keywords);
    }

    /**
     * indexing the file
     * @return solrdocID
     */
    @GetMapping("/index/{path}/{objectId}")
    public String indexing(@PathVariable("objectId") String objectId, @PathVariable("path") String path) throws SolrServerException, IOException {
        return docService.indexing(path, objectId);
    }
}
