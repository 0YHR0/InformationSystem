package com.yang.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller for the doc
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:25:58
 */
@RestController
@RequestMapping("doc")
public class DocController {
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name ---from doc-service";
    }

}
