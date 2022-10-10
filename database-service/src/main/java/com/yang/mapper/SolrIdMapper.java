package com.yang.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * This is a mapper for operating solrId
 * @author Liu Yuxin, Yang Haoran
 * @date 2022/8/14 56:21:42
 * @description
 */
public interface SolrIdMapper {
    /**
     * @param docId: the id of the document
     * @param solrDocId: the id of the document in solr
     * @return success or fail
     */
    @Update("update document set solrid = #{solrDocId} where docid = #{docId}")
    int UpdateSolrDocId(@Param("docId") int docId, @Param("solrDocId") String solrDocId);
}