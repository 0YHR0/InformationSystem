package com.yang.web;

import com.yang.pojo.Author;
import com.yang.pojo.Metadata;
import com.yang.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * get the request which related to operate the postgresql
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:28:50
 */
@RestController
@RequestMapping("postgresql")
public class PostgresqlController {

    @Autowired
    private AuthorService authorService;

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
    public Author getAuthorByName(@PathVariable("name") String name){
        System.out.println("get author by name:" + name);
        return authorService.queryByName(name);

    }

    /**
     * create the doc in the db use metadata
     * @param simplemetadata
     * @return docid
     */
    @GetMapping("/createdoc/{metadata}")
    public int createDoc(@PathVariable("metadata") Metadata simplemetadata){
        //TODO ...
        return 0;
    }

}
