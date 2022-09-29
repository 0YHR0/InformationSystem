package com.yang.service;

import com.yang.mapper.AuthorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Author;

/**
 * The service operating the author of the doc
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:28:26
 */
@Service
public class AuthorService {

    @Autowired
    private AuthorMapper authorMapper;

    public Author queryByName(String name){
        return authorMapper.findByName(name);
    }
}
