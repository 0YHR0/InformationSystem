package com.yang.servcie;

import com.yang.pojo.Doc;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ExpandParams;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.ContentStreamBase.FileStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.File;


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
        SolrQuery query =  new SolrQuery(keywords);
        System.out.println("-------------------------------------------query--------------------------------");
        QueryResponse response = new QueryResponse();
        try {
            response = solrClient.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SolrDocumentList documents = response.getResults();
        for(SolrDocument document : documents) {
            System.out.println(document);
          }
        ArrayList<Doc> res = new ArrayList<Doc>();
        //Doc e = new Doc(metadata, path, ObjectId, docId)
        return res;
    }


    /**
     * indexing the file
     * @return solrdocID
     */
    public String indexing(String path, String objectId) throws SolrServerException, IOException {
        String completePath = filePath + path + objectId;
        System.out.println(completePath);
        //ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
        //String tstPath = "C:/Users/Soheb/OneDrive/Desktop/tt.txt";
        //File file = new File(tstPath);
        //ContentStreamBase.FileStream fs = new FileStream(new File(tstPath));
        //up.addFile(new File(tstPath), "");
        ///up.setWaitSearcher(false);
        //up.setMethod(METHOD.POST);
        //up.addContentStream(fs);
        //up.setParam("literal.id", objectId);
        //up.setParam("commitWithin", "1000");
        //up.setParam("overwrite", "true");
        //up.setParam("wt", "json");
        //up.setParam("uprefix", "attr_");
        //up.setParam("fmap.content", "attr_content");
        //up.setParam("commit", "true");
        //up.setParam("defaultField", "true");
        //up.setCommitWithin(1000);
        //up.setAction(UpdateRequest.ACTION.COMMIT, true, true);
        //up.setParam(ExpandParams.EXPAND, "true");
        //up.addFile(file, "text/plain");
        //up.setParam("literal.id", objectId);
        //NamedList<Object> res = solrClient.request(up);
        //UpdateResponse res = up.process(solrClient);
        //solrClient.request(up);
        //System.out.println(res);
        String ftype = path.substring(path.length() - 4);
        System.out.println(ftype);
        String server = "http://129.69.209.197:30002/solr/testcore/update/extract?commitWithin=1000&overwrite=true&wt=json&literal.id=" + objectId;
        URL url = new URL(server);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        if(ftype.equals(".pdf")){
            http.setRequestProperty("Content-type", "application/pdf");
        } else if(ftype.equals(".txt")){
            http.setRequestProperty("Content-type", "text/plain");
        } else if(ftype.equals(".csv")){
            http.setRequestProperty("Content-type", "test/csv");
        } else if(ftype.equals(".doc")){
            http.setRequestProperty("Content-type", "application/msword");
        } else {
            http.setRequestProperty("Content-type", "application/octet-stream");
        }

        //http.setRequestProperty("Content-type", "application/pdf");

        String data = "C:/Users/Soheb/OneDrive/Documents/pdf-test.pdf";

        //byte[] out = data.getBytes(StandardCharsets.UTF_8);

        byte[] out;

        out = Files.readAllBytes(Paths.get(data));

        OutputStream stream = http.getOutputStream();
        stream.write(out);
        stream.flush();
        stream.close();

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();

        //QueryResponse rsp = solrClient.query(new SolrQuery("id:" + objectId));
     
        //System.out.println(rsp);
        //http://129.69.209.197:30002/solr/testcore/update/extract?commitWithin=1000&overwrite=true&wt=json&literal.id=test03
        
        return objectId;
    }


}
