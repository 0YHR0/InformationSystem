package com.yang.service;

import com.yang.mapper.AuthorMapper;
import com.yang.mapper.MetadataMapper;
import com.yang.pojo.Author;
import com.yang.pojo.Doc;
import com.yang.pojo.Metadata;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YHR
 * @date 2022/8/14 23:21:36
 * @description
 */

@Service
public class MetadataService {
    @Autowired
    public MetadataMapper metadataMapper;

    public int createDoc(Metadata metadata, int docId, int objectId){
        return  metadataMapper.createDoc(metadata, docId, objectId);
    }

    public List<Doc> queryDocByMetadata(Metadata metadata){
        return metadataMapper.queryDocByMetadata(metadata);
    }
}
