package com.yang.mapper;

import com.yang.pojo.Metadata;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author Liu Yuxin, Yang Haoran
 * @date 2022/8/14 56:21:42
 * @description
 */
public interface SolrIdMapper {
    /**
     * TODO...
     * @param docId
     * @param solrDocId
     * @return
     */
    @Update("xxxx #{docId} #{solrDocId}")
    int UpdateSolrDocId(@Param("docId") int docId, @Param("solrDocId") String solrDocId);
}