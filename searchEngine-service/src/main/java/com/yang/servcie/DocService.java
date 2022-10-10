package com.yang.servcie;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import com.emc.ecs.nfsclient.nfs.io.NfsFileInputStream;
import com.emc.ecs.nfsclient.nfs.nfs3.Nfs3;
import com.emc.ecs.nfsclient.rpc.CredentialUnix;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedInputStream;


/**
 * @author Soheb, Yang Haoran
 * @date 2022/8/15 00:12:02
 * @description
 */
@Service
public class DocService {

    @Autowired
    private SolrClient solrClient;

    @Value("${nfs.filepath}")
    String NFS_DIR;

    @Value("${nfs.ip}")
    String NFS_IP;

    @Value("${filetype.unknown}")
    String ctype;

    @Value("#{${filetype.known}}")
    Map<String,String> fileType;

    @Value("${spring.data.solr.host}")
    String solrHost;

    @Value("${spring.data.solr.command}")
    String solrCommand;

    /**
     * Get the article by keywords
     * @param keywords
     * @return a list of solrdocId(ObjectId)
     */
    public List<String> querySolr(String keywords){
        SolrQuery query =  new SolrQuery(keywords).setRows(50);
        System.out.println("-------------------------------------------query--------------------------------");
        ArrayList<String> res = new ArrayList<String>();
        QueryResponse response = new QueryResponse();
        try {
            response = solrClient.query(query);
            if (response.getResults().getNumFound() > 50) {
                SolrQuery largeQuery =  new SolrQuery(keywords).setRows((int) response.getResults().getNumFound()); 
                response = solrClient.query(largeQuery);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        SolrDocumentList documents = response.getResults();
        for(SolrDocument document : documents) {
            if(document.getFieldValue("id") != null)
            {
                res.add((String) document.getFieldValue("id"));
            }
            
          }
        //Doc e = new Doc(metadata, path, ObjectId, docId)
        return res;
    }


    /**
     * indexing the file
     * @return solrdocID,status_of_indexing
     */
    public String indexing(String path, String objectId) throws SolrServerException, IOException {
        InputStream inputStream = null;
        String rMsg = objectId + ",Error";
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
        String ftype = objectId.split("\\.")[1];
        //System.out.println(ftype);
        //String server = "http://129.69.209.197:30002/solr/testcore/update/extract?commitWithin=1000&overwrite=true&wt=json&literal.id=" + objectId;
        String server = solrHost + solrCommand + "&literal.id=" + objectId;
        System.out.println(server);
        URL url = new URL(server);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        for (Map.Entry<String, String> entry : fileType.entrySet()) {
            if(ftype.equals(entry.getKey())){
                http.setRequestProperty("Content-type", entry.getValue());
                break;
            }
            http.setRequestProperty("Content-type", ctype);
        }

        try{
            Nfs3 nfs3 = new Nfs3(NFS_IP, NFS_DIR, new CredentialUnix(0, 0, null), 3);
            System.out.println("path = " + path + "/" + objectId);
            Nfs3File nfsFile = new Nfs3File(nfs3, "/" + objectId);
            inputStream = new BufferedInputStream(new NfsFileInputStream(nfsFile));
            byte[] out = IOUtils.toByteArray(inputStream);
            //byte[] data = new byte[inputStream.available()];
            //inputStream.read(data);
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            stream.flush();
            stream.close();

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            if(http.getResponseCode() == 200){
                rMsg = objectId + ",indexed";
            }
            http.disconnect();

        } catch (IOException ex){
            ex.printStackTrace();
        }
        //QueryResponse rsp = solrClient.query(new SolrQuery("id:" + objectId));
        return rMsg;
    }


}
