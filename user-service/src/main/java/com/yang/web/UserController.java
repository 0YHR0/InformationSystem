package com.yang.web;

import com.yang.client.DatabaseClient;
import com.yang.client.SearchEngineClient;
import com.yang.pojo.Doc;
import com.yang.pojo.Metadata;
import com.yang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    public UserService userService;

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
        String objectId = userService.storeDoc(file);

        /**
         * create the metadata of the file to db
         */
        System.out.println("metadata:" + metadata);
        int docId = databaseClient.createDocToDB(metadata, filePath, objectId);

        /**
         * indexing the doc
         */

        String solrDocId = "xxx";
        searchEngineClient.indexing(filePath, objectId);

        /**
         * update solr doc ID
         */
        System.out.println("docId: " + docId + "solrdocId: " + solrDocId);
        int status = databaseClient.updateSolrDocId(docId, solrDocId);

        return "OK";
    }

    /**
     * Search the doc that the user needed
     * @param metadata
     * @param keywords
     * @return
     */
    @GetMapping("/search/{keywords}")
    public List<Doc> searchDoc(@RequestParam("metadata") Metadata metadata, @PathVariable("keywords") String keywords){
        System.out.println("search---> metadata: " + metadata + "keywords: " + keywords);
        List<Doc> docs = new ArrayList<>();
        /**
         * query db for metadata
         */
        List<Doc> docFromDB = databaseClient.queryDocByMetadata(metadata);
        docs.addAll(docFromDB);
        /**
         * query searching engine for keywords
         */
        List<Doc> docFromSE = searchEngineClient.querySolr(keywords);
        docs.addAll(docFromSE);

        return docs;
    }

    /**
     * Get the file that the user clicks
     * @param path
     */
    @GetMapping("/getFile")
    public void getFile(@RequestParam("path") String path, @RequestParam("objectId") String objectId, HttpServletResponse response) throws IOException {
        userService.getFile(path, objectId, response);
    }
}
