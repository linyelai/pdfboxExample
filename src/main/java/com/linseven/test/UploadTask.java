package com.linseven.test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.mongodb.client.MongoCollection;
import lombok.SneakyThrows;
import com.linseven.SaveUploadHistoryTask;
import org.bson.Document;

import java.io.*;
import java.util.Date;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/5/18 11:46
 */
public class UploadTask  implements  Runnable{

    private Document document;
    private static final String dir = "F:\\bidcar";
    private SaveUploadHistoryTask saveUploadHistoryTask;
    //
   // private static final String dir = "\\\\PS2022XXOQOOCG\\e\\bitCars";
    private MongoCollection mongoCollection;
    public UploadTask(Document document, MongoCollection mongoCollection, SaveUploadHistoryTask saveUploadHistoryTask){
        this.document = document;
        this.mongoCollection = mongoCollection;
        this.saveUploadHistoryTask = saveUploadHistoryTask;

    }

    @Override
    public void run() {


        try {
            AmazonS3 amazonS3 = AmazonS3ClientBuilder.defaultClient();
            String path =  (String)this.document.get("full_path");
            //path = path.replace("\\\\PS2022XXOQOOCG\\e","\\\\PS2022XXOQOOCG\\d");
            FileInputStream fileInputStream = new FileInputStream(path);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fileInputStream.read(buf)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            fileInputStream.close();
            int size = byteArrayOutputStream.size();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            String key = "uscarjunker.com/vinImages/" + this.document.get("path");
            String bucket = "static-lib";
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            metadata.setContentLength(size);
            metadata.setContentDisposition("filename=" + this.document.get("path"));
            metadata.setLastModified(new Date());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, byteArrayInputStream, metadata);
            putObjectRequest.setStorageClass(StorageClass.Standard);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
            //
            this.document.put("status", 1);
            //this.saveUploadHistoryTask.add(this.document);
            System.out.println(this.document.get("path"));
            Document filter = new Document();
            filter.put("_id", this.document.get("_id"));
            Document updateOperation = new Document();
            updateOperation.put("$set", this.document);
            this.mongoCollection.updateOne(filter, updateOperation);
//            System.out.println(this.document.get("path"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
