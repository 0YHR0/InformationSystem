package com.yang.servcie;

import com.yang.pojo.Doc;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YHR
 * @date 2022/8/15 00:12:02
 * @description
 */
@Service
public class DocService {

    @Autowired
    private SolrClient solrClient;

    @Value("${nfs.filepath}")
    String filePath;

    /**
     * Get the article by keywords
     * @param keywords
     * @return a list of docs
     */
    public List<Doc> querySolr(String keywords){
        SolrQuery query =  new SolrQuery("*:" + keywords);
        System.out.println("-------------------------------------------query--------------------------------");
        QueryResponse response = new QueryResponse();
        try {
            response = solrClient.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Todo...
         */
        return new ArrayList<Doc>();
    }


    /**
     * indexing the file
     * @return solrdocID
     */
    public String indexing(String objectId) throws SolrServerException, IOException {
        String path = filePath + objectId;
        /**
         * Todo...
         */
        SolrInputDocument doc = new SolrInputDocument();
        UpdateResponse updateResponse = solrClient.add("techproducts", doc);
        return "0";
    }


}
