package com.yang.web;

import com.yang.client.DatabaseClient;
import com.yang.client.SearchEngineClient;
import com.yang.pojo.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * get the request which related to the user
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:30:21
 */
@RestController
@RequestMapping("user")
public class UserController {

    private String suffixName;

    @Value("${nfs.filepath}")
    String filePath;

    @Autowired
    public DatabaseClient databaseClient;

    @Autowired
    public SearchEngineClient searchEngineClient;

    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name ---from user-service";
    }

    /**
     * Create and index the file from the user
     * @param file
     * @param metadata
     * @return
     */
    @PostMapping("/create")
    public String createDoc(@RequestParam("file") MultipartFile file, @RequestParam("metadata") Metadata metadata){

        /**
         * Store the file to fs
         */
        if(file.isEmpty()) {
            return "You upload an empty file";
        }
        //file.getSize()文件大小判断可用
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        //文件上传后的路径
        filename= UUID.randomUUID()+ suffixName;

        File dest=new File(filePath+filename);
        System.out.println(dest);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

        /**
         * create the metadata of the file to db
         */
        System.out.println("metadata:" + metadata);
        int docId = databaseClient.createDocToDB(metadata, filePath, filename);

        /**
         * indexing the doc
         */

        String solrDocId = "xxx";
        searchEngineClient.indexing(filePath + filename);




        /**
         * update solr doc ID
         */
        System.out.println("docId: " + docId + "solrdocId: " + solrDocId);
        int status = databaseClient.updateSolrDocId(docId, solrDocId);

        return "OK";
    }

    /**
     * Search the doc that the user needed
     * @param author
     * @param keywords
     * @return
     */
    @GetMapping("/search/{author}")
    public String searchDoc(@PathVariable("author") String author, @RequestParam("keywords") String keywords){
        System.out.println("search---> author: " + author + "keywords: " + keywords);
        return "0";
    }
}
