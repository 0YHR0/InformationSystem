package com.yang.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * get the request which related to operate the solr cloud
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:29:50
 */
@RestController
@RequestMapping("solr")
public class SolrController {
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name --- from solr-service";
    }
}
