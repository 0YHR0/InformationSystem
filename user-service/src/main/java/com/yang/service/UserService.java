package com.yang.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author YHR
 * @date 2022/8/14 15:21:02
 * @description
 */
@Service
public class UserService {


    @Value("${nfs.filepath}")
    String filePath;

    /**
     * Store the file the user uploaded
     * @param file
     * @return
     */
    public String storeDoc(MultipartFile file){
        if(file.isEmpty()) {
            return "You upload an empty file";
        }
        //file.getSize()文件大小判断可用
        String objectId = file.getOriginalFilename();
        String suffixName = objectId.substring(objectId.lastIndexOf("."));
        //文件上传后的路径
        objectId= UUID.randomUUID()+ suffixName;

        File dest=new File(filePath+objectId);
        System.out.println(dest);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
        return objectId;
    }
    /**
     * Get the file te user choose
     * @param path
     * @param objectId
     * @param response
     * @throws IOException
     */
    public void getFile(String path,String objectId, HttpServletResponse response) throws IOException {
        InputStream inputStream = new FileInputStream(path + objectId);
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(path).getName();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[1024];
        int len;
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }
}
