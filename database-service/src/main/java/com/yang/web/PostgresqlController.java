package com.yang.web;


import com.yang.service.AuthorService;
import com.yang.service.DocumentService;
import com.yang.service.MetadataService;
import com.yang.service.SolrIdService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Author;
import pojo.Doc;
import pojo.Metadata;


import java.util.List;

/**
 * get the request which related to operate the postgresql
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:28:50
 */
@RestController
@CrossOrigin
@RequestMapping("postgresql")
public class PostgresqlController {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private SolrIdService solrIdService;
    @Autowired
    private DocumentService documentService;

    /**
     * just for test
     * @param id: test
     * @return test
     */
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name --- from postgresql-service";
    }

    /**
     * Get the author by name
     * @param name: name of the author
     * @return author
     */
    @GetMapping("/author/{name}")
    public Author getAuthorByName(@ApiParam("The name of the author") @PathVariable("name") String name){
        System.out.println("get author by name:" + name);
        return authorService.queryByName(name);

    }

    /**
     * create the doc in the db use metadata
     * @param metadata: metadata of the document
     * @return docid
     */
    @PostMapping("/createDoc/{path}/{objectId}")
    public int createDoc(@RequestBody Metadata metadata, @PathVariable("path") String path, @PathVariable("objectId") String objectId){
        String actualPath = path.replaceAll("@","/");
        System.out.println("create: metadata " + metadata + "path: " + actualPath + "objectId: " + objectId);
        return metadataService.createDoc(metadata, actualPath, objectId);
    }

    /**
     * update the solrdocId to the db
     * @param docId: the id of the document
     * @param solrDocId: id of the solr doc
     * @return status
     */
    @GetMapping("/updateSolrDocId/{docId}/{solrDocId}")
    public int updateSolrDocId(@PathVariable("docId") int docId, @PathVariable("solrDocId") String solrDocId){
        System.out.println("update: docId" + docId + "solrdocId" + solrDocId);
        return solrIdService.UpdateSolrDocId(docId, solrDocId);
    }

    /**
     * queryDocByMetadata
     * @param authorName: name of the author
     * @param date: date of the document
     * @param title: title of the document
     * @return a list of docs
     */
    @GetMapping("/queryByMetadata")
    public List<Doc> queryDocByMetadata(@RequestParam("authorName") String authorName, @RequestParam("date") String date, @RequestParam("title")String title){
        System.out.println("query by authorname: " + authorName);
        return metadataService.queryDocByMetadata(authorName, date, title);
    }

    /**
     * query by solr doc Id
     * @param objectIds: ObjectId
     * @return result
     */
    @GetMapping("/queryBySolrDocId")
    public List<Doc> queryDocBySolrDocId(@RequestParam List<String> objectIds){
        for(String objectId:objectIds){
            System.out.println("query by solr doc Id: " + objectId);
        }
//        System.out.println(objectIds.toArray().toString());
        return metadataService.queryDocBySolrDocId(objectIds);
    }

    /**
     * delete the file by objectId
     * @param objectId
     * @return
     */
    @DeleteMapping("/deleteFile")
    public int deleteFile(@RequestParam String objectId){
        return documentService.deleteFile(objectId);

    }

}
