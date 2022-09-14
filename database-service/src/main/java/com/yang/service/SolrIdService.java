package com.yang.service;

import com.yang.mapper.SolrIdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Liu Yuxin, Yang Haoran
 * @date 2022/8/14 32:21:42
 * @description
 */
@Service
public class SolrIdService {

    @Autowired
    SolrIdMapper solrIdMapper;

    public int UpdateSolrDocId(int docId, String solrDocId){
        return solrIdMapper.UpdateSolrDocId(docId, solrDocId);
    }


}
