package com.yang.mapper;

import com.yang.pojo.Author;
import com.yang.pojo.Metadata;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
/**
 * This is a mapper for operating metadata of the doc
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:27:23
 */
public interface MetadataMapper {

    /**
     * Todo...
     * createDocWithMetadata
     * @param metadata
     * @return docid of the doc
     */
    @Insert("xxxx #{metadata}")
    int createDocWithMetadata(@Param("metadata") Metadata metadata);
}
