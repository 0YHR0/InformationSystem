package com.yang.service;

import com.yang.mapper.DocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The service operating doc
 * @author Yang Haoran
 * @date 2022/10/12 28:0:15
 * @description
 */
@Service
public class DocumentService {
    @Autowired
    DocMapper docmapper;

    public int deleteFile(String objectId){
        System.out.println("delete: " + objectId);
        int result = docmapper.deleteFile(objectId);
        System.out.println("result: " + result);
        return result;
    }
}
