package com.yang.web;

import com.yang.pojo.Author;
import com.yang.pojo.Doc;
import com.yang.pojo.Metadata;
import com.yang.service.AuthorService;
import com.yang.service.MetadataService;
import com.yang.service.SolrIdService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * get the request which related to operate the postgresql
 * @Author: Liu Yuxin, Yang Haoran
 * @Date: 01-08-2022 11:28:50
 */
@CrossOrigin
@RestController
@RequestMapping("postgresql")
public class PostgresqlController {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private SolrIdService solrIdService;

    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name --- from postgresql-service";
    }

    /**
     * Get the author by name
     * @param name
     * @return author
     */
    @GetMapping("/author/{name}")
    public Author getAuthorByName(@ApiParam("The name of the author") @PathVariable("name") String name){
        System.out.println("get author by name:" + name);
        return authorService.queryByName(name);

    }

    /**
     * create the doc in the db use metadata
     * @param metadata
     * @return docid
     */
    @PostMapping("/createDoc")
    public int createDoc(@RequestParam("metadata") Metadata metadata, @RequestParam("path") String path, @RequestParam("objectId") String objectId){
        System.out.println("create: metadata " + metadata + "path: " + path + "objectId: " + objectId);
        return metadataService.createDoc(metadata, path, objectId);
    }

    /**
     * update the solrdocId to the db
     * @param docId
     * @param solrDocId
     * @return
     */
    @PutMapping("/updateSolrDocId")
    public int updateSolrDocId(@RequestParam("docId") int docId, @RequestParam("solrDocId") String solrDocId){
        System.out.println("update: docId" + docId + "solrdocId" + solrDocId);
        return solrIdService.UpdateSolrDocId(docId, solrDocId);
    }

    /**
     * queryDocByMetadata
     * @param metadata
     * @return
     */
    @PostMapping("/query")
    public List<Doc> queryDocByMetadata(@RequestBody Metadata metadata){
        System.out.println("query by metadata: " + metadata);
        return metadataService.queryDocByMetadata(metadata);
    }

}
