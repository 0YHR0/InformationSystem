package com.yang.web;

import org.springframework.web.bind.annotation.*;
/**
 * get the request which related to the user
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:30:21
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("/test/{id}")
    public String test(@PathVariable("id") String id){
        System.out.println(id);
        return "success! Your name is: " + id + "'s name ---from user-service";
    }
}
