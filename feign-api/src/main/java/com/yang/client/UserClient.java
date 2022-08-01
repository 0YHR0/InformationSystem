package com.yang.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * call the user service
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:26:53
 */
@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/user/test/{id}")
    String sendRequestToUserService(@PathVariable("id") String id);

}
