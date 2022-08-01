package com.yang.service;

import com.yang.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchingService {

    @Autowired
    public UserClient userClient;

    /**
     * get the user from the user-service
     * @param id
     * @return username
     */
    public String getUserById(String id){
        return  userClient.sendRequestToUserService(id);
    }
}
