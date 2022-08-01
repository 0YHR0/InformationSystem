package com.yang.web;

import com.yang.service.SearchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    public SearchingService searchingService;

    @GetMapping("/test")
    public String test(@RequestParam("keywords") String keywords){
        System.out.println(keywords);
        return "success! your key word is: " + keywords;
    }

    @GetMapping("/user")
    public String getUser(@RequestParam("id") String id){
        return searchingService.getUserById(id);
    }
}
