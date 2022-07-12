package com.yang.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("/test")
    public String test(@RequestParam("name") String name){
        System.out.println(name);
        return "success! Your name is: " + name;
    }
}
