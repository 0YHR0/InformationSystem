package com.yang.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import pojo.Doc;
import pojo.Metadata;

import java.util.List;

/**
 * This is a mapper for operating metadata of the doc
 * @Author: Liu Yuxin, Yang Haoran
 * @Date: 01-08-2022 11:27:23
 */
public interface MetadataMapper {

    /**
     * createDocWithMetadata
     * @param metadata
     * @return docid of the doc
     */
    @Insert("insert into organization(orgid,name,address) values(#{metadata.organization.orgId},#{metadata.organization.name},#{metadata.organization.address});\n"+
            "insert into publication(pubid,orgid,title,date) values(#{metadata.pubid},#{metadata.organization.orgId},#{metadata.title},#{metadata.date});\n"+
            "insert into author(authorid,name,pubid) values(#{metadata.author.authorid}, #{metadata.author.name}, #{metadata.pubid});\n"+
            "insert into nfsserver(mntpath) values(#{path});\n"+
            "insert into document(pubid,type,size,filename,objectid) values(#{metadata.pubid},#{metadata.fileType},#{metadata.fileSize},#{metadata.filename},#{objectId});\n"+
            "update document set nfsid =(select nfsid from nfsserver where mntpath='${path}' LIMIT 1) where objectid='${objectId}'")
    int createDoc(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    /**
     *
     * same with the above one function, used to test individually
    @Insert("insert into organization(orgid,name,address) values(#{metadata.organization.orgId},#{metadata.organization.name},#{metadata.organization.address})")
    int createDocToOrganozation(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    @Insert("insert into publication(pubid,orgid,title,date) values(#{metadata.pubid},#{metadata.organization.orgId},#{metadata.title},#{metadata.date})")
    int createDocToPublication(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    @Insert("insert into author(authorid,name,pubid) values(#{metadata.author.authorid}, #{metadata.author.name}, #{metadata.pubid})")
    int createDocToAuthor(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    @Insert("insert into nfsserver(mntpath) values(#{path})")
    int createDocToNfsServer(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    @Insert("insert into document(pubid,type,size,filename,objectid) values(#{metadata.pubid},#{metadata.fileType},#{metadata.fileSize},#{metadata.filename},#{objectId})")
    int createDocToDocument(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    @Update("update document set nfsid =(select nfsid from nfsserver where mntpath='${path}' LIMIT 1) where objectid='${objectId}'")
    int updateDocToDocument(@Param("metadata") Metadata metadata, @Param("path") String path, @Param("objectId") String objectId);

    **/



    /**
     * queryDocByMetadata
     * @param authorName: name of the author
     * @param date: date of the document
     * @param title: title of the document
     * @return a list of documents
     */
    @Select("select distinct a.name, p.title, p.date, d.filename,d.size, d.type, n.mntpath, d.objectid, d.docid from author a, publication p, document d, nfsserver n\n" +
            "where a.pubid =p.pubid and p.pubid =d.pubid and a.name =#{authorName} and p.title =#{title} and p.date=#{date} and d.nfsid= n.nfsid")
    List<Doc> queryDocByMetadata(@Param("authorName") String authorName, @Param("date")String date, @Param("title")String title);

    /**
     * query document by solr doc Id
     * @param solrdocid: ID of the solrdoc
     * @return the document needed
     */
    @Select("select distinct a.name, p.title, p.date, d.filename,d.size, d.type, n.mntpath, d.objectid, d.docid from author a, publication p, document d, nfsserver n where solrid=#{solrdocid} and d.nfsid=n.nfsid and a.pubid =p.pubid and p.pubid =d.pubid")
    Doc queryDocBySolrDocId(@Param("solrdocid") String solrdocid);
}
