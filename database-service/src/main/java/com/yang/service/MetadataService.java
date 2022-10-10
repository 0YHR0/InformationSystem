package com.yang.service;

import com.yang.mapper.AuthorMapper;
import com.yang.mapper.MetadataMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.Doc;
import pojo.Metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * The service operating the metadata of the doc
 * @author Yang Haoran
 * @date 2022/8/14 23:21:36
 */

@Service
public class MetadataService {
    @Autowired
    private MetadataMapper metadataMapper;

    /**
     * create the document using the metadata
     * @param metadata: the metadata of the document
     * @param path: the path of the file in nfs
     * @param objectId: the objectId of the document
     * @return status
     */
    public int createDoc(Metadata metadata, String path, String objectId){
//        System.out.println("createDoc: metadata" + metadata + " path: " + path + " objectId: " + objectId);
//        int result1 = metadataMapper.createDocToOrganozation(metadata, path, objectId);
//        System.out.println("createDocToOrganozation result: " + result1);
//        int result2 =metadataMapper.createDocToPublication(metadata, path, objectId);
//        System.out.println("createDocToPublication result: " + result2);
//        int result3 =metadataMapper.createDocToAuthor(metadata, path, objectId);
//        System.out.println("createDocToAuthor result: " + result3);
//        int result4 =metadataMapper.createDocToNfsServer(metadata, path, objectId);
//        System.out.println("createDocToNfsServer result: " + result4);
//        int result5 =metadataMapper.createDocToDocument(metadata, path, objectId);
//        System.out.println("createDocToDocument result: " + result5);
//        int result6 = metadataMapper.updateDocToDocument(metadata, path, objectId);
//        System.out.println("updateDocToDocument result: " + result6);
//        return  result1*result2*result3*result4*result5*result6;
        return metadataMapper.createDoc(metadata, path, objectId);
    }

    /**
     * query the document by metadata
     * @param authorName: name of the author
     * @param date: date of the document
     * @param title: title of the document
     * @return the query esult
     */
    public List<Doc> queryDocByMetadata(String authorName, String date, String title){
        return metadataMapper.queryDocByMetadata(authorName, date, title);
    }

    /**
     * query the document by solr doc Id
     * @param solrDocIds: solr doc Id
     * @return the query result
     */
    public List<Doc> queryDocBySolrDocId(List<String> solrDocIds){
        List<Doc> docs = new ArrayList<>();
        for (int i = 0; i < solrDocIds.size(); i++) {
            docs.add(metadataMapper.queryDocBySolrDocId(solrDocIds.get(i)));
        }
        return docs;
    }
}
