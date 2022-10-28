package com.yang.service;

import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import com.emc.ecs.nfsclient.nfs.io.NfsFileInputStream;
import com.emc.ecs.nfsclient.nfs.io.NfsFileOutputStream;
import com.emc.ecs.nfsclient.nfs.nfs3.Nfs3;
import com.emc.ecs.nfsclient.rpc.CredentialUnix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author Yang Haoran
 * @date 2022/8/14 15:21:02
 * @description
 */
@Service
public class UserService {


    @Value("${nfs.filepath}")
    String NFS_DIR;

    @Value("${nfs.ip}")
    String NFS_IP;

//    public String storeDocToNFS(MultipartFile file){
//
//    }

    /**
     * Store the file the user uploaded
     * @param file
     * @return
     */
    public String storeDoc(MultipartFile file) {
        File fileTemp = null;
        try {
            String originalFilename = file.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            fileTemp=File.createTempFile(filename[0], filename[1]);
            file.transferTo(fileTemp);
            fileTemp.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(file.isEmpty()) {
            return "You upload an empty file";
        }
        //file.getSize()
        String objectId = file.getOriginalFilename();
        String suffixName = objectId.substring(objectId.lastIndexOf("."));
        //file upload path
        objectId= UUID.randomUUID()+ suffixName;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Nfs3 nfs3 = new Nfs3(NFS_IP, NFS_DIR, new CredentialUnix(0, 0, null), 3);
            Nfs3File nfsFile = new Nfs3File(nfs3, "/" + objectId);
            try {
            inputStream = new BufferedInputStream(new FileInputStream(fileTemp));
            //open a a remote Nfs file output stream，copy the file to the destination
            outputStream = new BufferedOutputStream(new NfsFileOutputStream(nfsFile));

            //cache the memory
            byte[] buffer = new byte[1024];
            while ((inputStream.read(buffer)) != -1) {
                outputStream.write(buffer);
            }
            System.out.println("File upload finish！");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

//            file.transferTo(nfsFile);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
//        File dest=new File(filePath+objectId);
//        System.out.println(dest);
//        try {
//            file.transferTo(nfsFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return e.toString();
//        }
        return objectId;
    }
    /**
     * Get the file te user choose
     * @param objectId
     * @param response
     * @throws IOException
     */
    public void getFile(String objectId, HttpServletResponse response) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File fileTemp = null;
        try {
            Nfs3 nfs3 = new Nfs3(NFS_IP, NFS_DIR, new CredentialUnix(0, 0, null), 3);
            //create nfs upload project
            Nfs3File nfsFile = new Nfs3File(nfs3, "/" + objectId);
            fileTemp = File.createTempFile(objectId.substring(0, objectId.length() - 4), objectId.substring(objectId.length() - 4));

//            String localFileName = localDir + nfsFile.getName();

//            File localFile = new File(localFileName);
            inputStream = new BufferedInputStream(new NfsFileInputStream(nfsFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(fileTemp));


            byte[] buffer = new byte[1024];

            while (inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            System.out.println("The downloading cache complete！");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //------------------------------------
        InputStream resinputStream = new FileInputStream(fileTemp);
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = "file"+ objectId.substring(objectId.length() - 4);
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream resoutputStream = null;
        try {
            resoutputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[1024];
        int len;
        while ((len = resinputStream.read(b)) > 0) {
            resoutputStream.write(b, 0, len);
        }
        resinputStream.close();
        fileTemp.deleteOnExit();
    }


}
