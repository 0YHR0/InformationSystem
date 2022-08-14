package com.yang.web;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * get the request which related to operate the solr cloud
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:29:50
 */
@RestController
@RequestMapping("solr")
public class SolrController {

    @Autowired
    private SolrClient solrClient;

    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name --- from solr-service";
    }

    /**
     * Get the article by name
     * @param name
     * @return
     */
    @GetMapping("/query/{name}")
    public String querySolr(@PathVariable("name") String name){
        SolrQuery query =  new SolrQuery("name:" + name);
        System.out.println("-------------------------------------------query--------------------------------");
        QueryResponse response = new QueryResponse();
        try {
            response = solrClient.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    /**
     * indexing the file
     * @return solrdocID
     */
    @GetMapping("/index/{path}")
    public String indexing(@PathVariable("path") String path) throws SolrServerException, IOException {
        SolrInputDocument doc = new SolrInputDocument();
        UpdateResponse updateResponse = solrClient.add("techproducts", doc);
        return "0";

    }
}
