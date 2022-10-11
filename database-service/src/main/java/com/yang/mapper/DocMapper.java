package com.yang.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * This is a mapper for operating the doc
 * @author Yang Haoran
 * @date 2022/10/12 20:0:13
 * @description
 */
@Mapper
public interface DocMapper {
    /**
     * delete the file according to the objectid
     * @param objectId
     * @return
     */
    @Update("update document set isdeleted=true where objectId=#{objectId}")
    int deleteFile(@Param("objectId") String objectId);
}
