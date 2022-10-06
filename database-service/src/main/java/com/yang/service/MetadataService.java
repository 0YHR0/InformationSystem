package com.yang.service;

import com.yang.mapper.AuthorMapper;
import com.yang.mapper.MetadataMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Doc;
import pojo.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YHR
 * @date 2022/8/14 23:21:36
 * @description
 */

@Service
public class MetadataService {
    @Autowired
    private MetadataMapper metadataMapper;

    public int createDoc(Metadata metadata, String path, String objectId){
        return  metadataMapper.createDoc(metadata, path, objectId);
    }

    public List<Doc> queryDocByMetadata(String authorName, String date, String title){
        return metadataMapper.queryDocByMetadata(authorName, date, title);
    }
    public List<Doc> queryDocBySolrDocId(List<String> solrDocIds){
        List<Doc> docs = new ArrayList<>();
        for (int i = 0; i < solrDocIds.size(); i++) {
            docs.add(metadataMapper.queryDocBySolrDocId(solrDocIds.get(i)));
        }
        return docs;
    }
}
