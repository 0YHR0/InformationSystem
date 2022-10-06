package com.yang.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author YHR
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
    @Update("update document set solrid = #{solrDocId} where docid = #{docId}")
    int UpdateSolrDocId(@Param("docId") int docId, @Param("solrDocId") String solrDocId);
}