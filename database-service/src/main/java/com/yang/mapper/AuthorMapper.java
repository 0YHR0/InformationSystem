package com.yang.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pojo.Author;

/**
 * This is a mapper for operating author of the doc
 * @Author: Liu Yuxin, Yang Haoran
 * @Date: 01-08-2022 11:25:00
 */
@DS("replica")
public interface AuthorMapper {
    /**
     * find the author by name
     * @param name: the name of the author
     * @return author
     */
    @Select("select authorid,name from author where name = #{name}")
    Author findByName(@Param("name") String name);

}
