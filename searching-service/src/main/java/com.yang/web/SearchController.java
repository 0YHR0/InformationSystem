package com.yang.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {

    @GetMapping("/test")
    public String test(@RequestParam("keywords") String keywords){
        System.out.println(keywords);
        return "success! your key word is: " + keywords;
    }
}
