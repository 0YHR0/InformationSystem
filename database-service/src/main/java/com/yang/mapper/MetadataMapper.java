package com.yang.mapper;

import com.yang.pojo.Author;
import com.yang.pojo.Doc;
import com.yang.pojo.Metadata;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * This is a mapper for operating metadata of the doc
 * @Author: Liu Yuxin, Yang Haoran
 * @Date: 01-08-2022 11:27:23
 */
public interface MetadataMapper {

    /**
     * Todo...
     * createDocWithMetadata
     * @param metadata
     * @return docid of the doc
     */
    @Insert("xxxx #{metadata} #{path} #{objectId}")
    int createDoc(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    /**
     * Todo...
     * queryDocByMetadata
     * @param metadata
     * @return
     */
    @Select("xxxx #{metadata}")
    List<Doc> queryDocByMetadata(@Param("metadata") Metadata metadata);
}
