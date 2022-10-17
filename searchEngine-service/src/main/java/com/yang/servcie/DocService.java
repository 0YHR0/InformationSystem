package com.yang.servcie;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import com.emc.ecs.nfsclient.nfs.io.NfsFileInputStream;
import com.emc.ecs.nfsclient.nfs.nfs3.Nfs3;
import com.emc.ecs.nfsclient.rpc.CredentialUnix;
import pojo.SolrMetadata;

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
    public List<SolrMetadata> querySolr(String keywords){
        SolrQuery query =  new SolrQuery(keywords).setRows(50);
        System.out.println("-------------------------------------------query--------------------------------");
        Map<String, Map<String, ArrayList<String>>> res_all = new HashMap<>();
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
                System.out.println(document.getFieldValue("id"));
                res.add((String) document.getFieldValue("id") + "indexed");
                Map<String, ArrayList<String>> mData = new HashMap<String, ArrayList<String>>();
                if(document.getFieldValue("abstract") != null){
                    try{
                        mData.put("abstract", (ArrayList<String>) document.getFieldValue("abstract"));
                    } catch (Exception ex) {
                        ArrayList<String> tempA = new ArrayList<>();
                        tempA.add((String) document.getFieldValue("abstract"));
                        mData.put("abstract", tempA);
                    }
                } else {
                    ArrayList<String> tempA = new ArrayList<>();
                    tempA.add("No Abstract");
                    mData.put("abstract", tempA);
                }
                if(document.getFieldValue("keywords") != null){
                    try{
                        mData.put("keywords", (ArrayList<String>) document.getFieldValue("keywords"));
                    } catch (Exception ex) {
                        ArrayList<String> tempK = new ArrayList<>();
                        tempK.add((String) document.getFieldValue("abstract"));
                        mData.put("keywords", tempK);
                    }
                } else {
                    ArrayList<String> tempK = new ArrayList<>();
                    tempK.add("No Keywords");
                    mData.put("keywords", tempK);
                }
                res_all.put(document.getFieldValue("id") + "indexed", mData);
                //System.out.println(res_all.get(document.getFieldValue("id")).get("abstract"));
            }

        }
        ArrayList<SolrMetadata> solrMetadataArrayList = new ArrayList<>();
        //Doc e = new Doc(metadata, path, ObjectId, docId)
        //System.out.println(res_all);
        for (Map.Entry<String, Map<String, ArrayList<String>>> entry : res_all.entrySet()) { //res_all has the id + keywords + abstract
            solrMetadataArrayList.add(new SolrMetadata(entry.getKey(), entry.getValue().get("abstract").get(0), entry.getValue().get("keywords").get(0)));
            System.out.println(entry.getKey()); // has all the ids
            System.out.println(entry.getValue().get("abstract").get(0)); //has the abstract for the given id get(0) is to get the string from arraylist
            System.out.println(entry.getValue().get("keywords").get(0)); //has the keywords for the given id get(0) is to get the string form arraylist
            System.out.println("_______________________");
        }


        return solrMetadataArrayList;
    }


    /**
     * indexing the file
     * @return solrdocID,status_of_indexing
     * @throws TikaException
     * @throws SAXException
     */
    public String indexing(String path, String objectId) throws SolrServerException, IOException {
        InputStream inputStream = null;
        String rMsg = objectId + "Error";
        StringWriter any = new StringWriter();
        String Abstract = null;
        String kwords = null;
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


        try{
            Nfs3 nfs3 = new Nfs3(NFS_IP, NFS_DIR, new CredentialUnix(0, 0, null), 3);
            System.out.println("path = " + path + "/" + objectId);
            Nfs3File nfsFile = new Nfs3File(nfs3, "/" + objectId);
            inputStream = new BufferedInputStream(new NfsFileInputStream(nfsFile));
            byte[] out = IOUtils.toByteArray(inputStream);

            //byte[] data = new byte[inputStream.available()];
            //inputStream.read(data);

            File tempFile = File.createTempFile(objectId.split("\\.")[0], objectId.split("\\.")[1], null);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(out);

            FileInputStream fstream = new FileInputStream(tempFile);
            BodyContentHandler contenthandler = new BodyContentHandler(any);
            // Create an object of type Metadata to use
            Metadata data = new Metadata();
            // Create a context parser for the pdf document
            ParseContext context = new ParseContext();
            // PDF document can be parsed using the PDFparser
            // class
            PDFParser pdfparser = new PDFParser();
            // Method parse invoked on PDFParser class
            pdfparser.parse(fstream, contenthandler, data, context);

            // Printing the contents of the pdf document
            // using toString() method in java
            String content = contenthandler.toString();
            if(content.indexOf("Index Terms") != -1){
                Abstract = content.substring(content.indexOf("Abstract") + 9, content.indexOf("Index Terms") - 1);
                kwords = content.substring(content.indexOf("Index Terms") + 12, content.indexOf("INTRODUCTION") - 7);
                //System.out.println(kwords);
            }
            if(content.indexOf("Keywords") != -1){
                Abstract = content.substring(content.indexOf("Abstract") + 10, content.indexOf("Keywords") - 1);
                kwords = content.substring(content.indexOf("Keywords") + 10, content.indexOf("INTRODUCTION") - 7);
                //System.out.println(kwords);
            }

            //System.out.println("Extracting contents :" + content.substring(content.indexOf("Abstract") + 9, content.indexOf("Index Terms") - 1));
            String ftype = objectId.split("\\.")[1];
            //System.out.println(ftype);
            //String server = "http://129.69.209.197:30002/solr/testcore/update/extract?commitWithin=1000&overwrite=true&wt=json&literal.id=" + objectId;
            String server = solrHost + solrCommand + "&literal.id=" + objectId + "&literal.abstract=" + null + "&literal.keywords=" + null;
            //System.out.println(server);
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
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            stream.flush();
            stream.close();

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            int c = http.getResponseCode();
            http.disconnect();

            ((HttpSolrClient) solrClient).setParser(new XMLResponseParser());
            //UpdateRequest updateRequest = new UpdateRequest();
            //updateRequest.setAction( UpdateRequest.ACTION.COMMIT, false, false);
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", objectId);
            HashMap<String, String> valueA = new HashMap<String, String>();
            valueA.put("set", Abstract);
            document.addField("abstract", valueA);
            HashMap<String, String> valueK = new HashMap<String, String>();
            valueK.put("set", kwords);
            document.addField("keywords", valueK);
            //updateRequest.add(document);
            //UpdateResponse rsp = updateRequest.process(solrClient);
            solrClient.add(document);
            solrClient.commit();
            if(c == 200){
                rMsg = objectId + "indexed";
            }

        } catch (IOException | TikaException | SAXException ex){
            ex.printStackTrace();
        }

        //QueryResponse rsp = solrClient.query(new SolrQuery("id:" + objectId));
        return rMsg;
    }


}