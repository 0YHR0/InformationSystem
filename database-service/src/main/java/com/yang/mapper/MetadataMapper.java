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
    @Insert("insert into author(name,orgid) values(#{metadata.name},'1');\n" +
            "insert into libraryserver (docid,deleted) values(#{docId},'false');\n"+
            "insert into objecte (objectid, pubid, docid,nfsid) values(#{objectId},'100',#{docId},'111');\n"+
            "insert into catalogdb (authorid, pubid, docid) select authorid, pubid, docid from objecte o, author a where a.name = #{metadata.name} and o.objectid = #{objectId}")
    int createDoc(@Param("metadata") Metadata metadata, @Param("docId") int docid, @Param("objectId") int objectId);

    /**
     * Todo...
     * queryDocByMetadata
     * @param metadata
     * @return
     */
    @Select("select filename, name, mntpath, objectid, o.docid, solrid from\n" +
            "author a ,libraryserver l ,catalogdb c ,nfsserver n , objecte o\n" +
            "where a.authorid =c.authorid and c.docid =l.docid and l.docid =o.docid and o.nfsid =n.nfsid and a.name = #{metadata.name}")
    List<Doc> queryDocByMetadata(@Param("metadata") Metadata metadata);
}
