package com.yang.web;

import com.yang.client.DatabaseClient;
import com.yang.client.SearchEngineClient;
import com.yang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.Doc;
import pojo.Metadata;

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
    public String createDoc(@RequestParam("file") MultipartFile file, @ModelAttribute Metadata metadata){
        String objectId = userService.storeDoc(file);
//        return "111";

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
     * @param authorname
     * @param date
     * @param title
     * @param keywords
     * @return
     */
    @GetMapping("/search/{keywords}")
    public List<Doc> searchDoc(@RequestParam("authorname") String authorname, @RequestParam("date") String date, @RequestParam("title") String title, @PathVariable("keywords") String keywords){
        System.out.println("search---> authorname: " + authorname + "date" + date + "title" + title + "keywords: " + keywords);
        List<Doc> docs = new ArrayList<>();
        /**
         * query db for metadata
         */
        List<Doc> docFromDB = databaseClient.queryDocByMetadata(authorname, date, title);
        docs.addAll(docFromDB);
        /**
         * query searching engine for keywords
         */
        List<String> docIdsFromSE = searchEngineClient.querySolr(keywords);
        /**
         * query the doc details from database using the solrdocId
         */
        docs.addAll(databaseClient.queryDocBySolrDocId(docIdsFromSE));

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
