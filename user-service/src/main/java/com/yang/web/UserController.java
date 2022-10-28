package com.yang.web;

import com.yang.client.DatabaseClient;
import com.yang.client.SearchEngineClient;
import com.yang.service.UserService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.CompleteDoc;
import pojo.Doc;
import pojo.Metadata;
import pojo.SolrMetadata;

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
@CrossOrigin("*")
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
        String filepathForUrl = filePath.replaceAll("/", "@");
        System.out.println("filepathForURL" + filepathForUrl);
        int docId = databaseClient.createDocToDB(metadata, filepathForUrl, objectId);

        /**
         * indexing the doc
         */
        String solrDocId = searchEngineClient.indexing(filepathForUrl, objectId);

        /**
         * update solr doc ID
         */
        System.out.println("docId: " + docId + "solrdocId: " + solrDocId);
        int status = databaseClient.updateSolrDocId(docId, solrDocId);

        return "status: " + status;
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
    public List<CompleteDoc> searchDoc(@RequestParam("authorname") String authorname, @RequestParam("date") String date, @RequestParam("title") String title, @RequestParam(value = "addData", required = false) String addData, @PathVariable("keywords") String keywords){
        System.out.println("search---> authorname: " + authorname + "date: " + date + "title: " + title + "addData: " + addData + "keywords: " + keywords);
        List<Doc> docs = new ArrayList<>();
        System.out.println("queryDocByMetadata--->start");
        /**
         * query db for metadata
         */
        List<Doc> docFromDB = databaseClient.queryDocByMetadata(authorname, date, title, addData);
//        docs.addAll(docFromDB);
        System.out.println("docs from db" + docFromDB);
        /**
         * query searching engine for keywords
         */
        System.out.println("querySolr--->start");
        List<SolrMetadata> docsFromSE = searchEngineClient.querySolr(keywords);
        System.out.println("docs from se:" + docsFromSE);
        /**
         * query the doc details from database using the solrdocId
         */
//        List<Integer> docIdFromDb = new ArrayList<>();
//        for(Doc docdb: docs){
//            docIdFromDb.add(docdb.docId);
//        }
//        List<String> docIdsFromSE = new ArrayList<>();
        List<CompleteDoc> result = new ArrayList<>();
        for(Doc doc: docFromDB){
            for(SolrMetadata solrMetadata: docsFromSE){
                if((doc.ObjectId + "indexed").equals(solrMetadata.getSolrId())){
                    CompleteDoc cd = new CompleteDoc();
                    cd.setDocId(doc.docId);
                    cd.setDate(doc.date);
                    cd.setFilename(doc.filename);
                    cd.setIsdeleted(doc.isdeleted);
                    cd.setMntpath(doc.mntpath);
                    cd.setObjectId(doc.ObjectId);
                    cd.setPublication_json(doc.publication_json);
                    cd.setSize(doc.size);
                    cd.setTitle(doc.title);
                    cd.setName(doc.name);
                    cd.setType(doc.type);
                    cd.setAbstra(solrMetadata.getAbstra());
                    cd.setKeywords(solrMetadata.getKeywords());
                    result.add(cd);
                }
            }


        }


        //---------------------------------------------
//        for(SolrMetadata metadata:docsFromSE){
//            docIdsFromSE.add(metadata.getSolrId());
//        }
//        List<Doc> docBySolrDocId = databaseClient.queryDocBySolrDocId(docIdsFromSE);
//
//        for(Doc doc: docBySolrDocId){
//            System.out.println("querydocbysolrdocid: " + doc);
//            if(doc != null){
//                if(!docIdFromDb.contains(doc.docId)){
//                    CompleteDoc cd = new CompleteDoc();
//                    cd.setDocId(doc.docId);
//                    cd.setDate(doc.date);
//                    cd.setFilename(doc.filename);
//                    cd.setIsdeleted(doc.isdeleted);
//                    cd.setMntpath(doc.mntpath);
//                    cd.setObjectId(doc.ObjectId);
//                    cd.setPublication_json(doc.publication_json);
//                    cd.setSize(doc.size);
//                    cd.setTitle(doc.title);
//                    cd.setName(doc.name);
//                    cd.setType(doc.type);
//
//
//                    result.add();
//                    System.out.println("adddocId: " + doc.docId);
//                    docs.add(doc);
//                }
//            }
//
//        }
//        docs.addAll(databaseClient.queryDocBySolrDocId(docIdsFromSE));
        System.out.println("search results--->" + result);

        return result;
    }

    /**
     * Get the file that the user clicks
     * example: objectId=1111.txt
     */
    @GetMapping("/getFile")
    public void getFile(@RequestParam("objectId") String objectId, HttpServletResponse response) throws IOException {
        userService.getFile(objectId, response);
    }

    /**
     * delete the file that the user needed
     * @param objectId
     * @return
     */
    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam("objectId") String objectId){
        if(databaseClient.deleteFile(objectId) == 1) {
            return "success";
        } else{
            return "fail";
        }
    }
}
