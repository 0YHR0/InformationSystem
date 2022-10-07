package com.yang.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pojo.Doc;
import pojo.Metadata;

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
    @Insert("insert into organization(orgid,name,address) values(#{metadata.organization.orgId},#{metadata.organization.name},#{metadata.organization.address});\n"+
            "insert into publication(pubid,orgid,title,date) values(#{metadata.pubid},#{metadata.organization.orgId},#{metadata.title},#{metadata.date});\n"+
            "insert into author(authorid,name,pubid) values(#{metadata.author.authorid}, #{metadata.author.name}, #{metadata.pubid});\n"+
            "insert into nfsserver(mntpath) values(#{path});\n"+
            "insert into document(pubid,type,size,filename,objectid) values(#{metadata.pubid},#{metadata.fileType},#{metadata.fileSize},#{metadata.filename},#{objectId});\n"+
            "update \"document\" set nfsid =(select nfsid from nfsserver where mntpath=#{path}) where objectid=#{objectId}")
    int createDoc(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    /**
     * Todo...
     * queryDocByMetadata
     * @param authorName
     * @param date
     * @param title
     * @return
     */
    @Select("select distinct a.name, p.title, p.date, d.filename,d.size, d.type, n.mntpath, d.objectid, d.docid from author a, publication p, document d, nfsserver n\n" +
            "where a.pubid =p.pubid and p.pubid =d.pubid and a.name =#{authorName} and p.title =#{title} and p.date=#{date} and d.nfsid= n.nfsid")
    List<Doc> queryDocByMetadata(@Param("authorName") String authorName, @Param("date")String date, @Param("title")String title);

    /**
     * Todo..
     * queryDocBySolrDocId
     */
    @Select("select distinct a.name, p.title, p.date, d.filename,d.size, d.type, n.mntpath, d.objectid, d.docid from author a, publication p, document d, nfsserver n where solrid=#{solrdocid} and d.nfsid=n.nfsid and a.pubid =p.pubid and p.pubid =d.pubid")
    Doc queryDocBySolrDocId(@Param("solrdocid") String solrdocid);
}
