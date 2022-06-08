package com.linseven;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/5/26 17:31
 */
public class InitDataThread extends  Thread {

    @Override
    public void run() {

        MongoClient mongoClient = new MongoClient("192.168.0.122",27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("bidcars17");
        MongoCollection mongoCollection = mongoDatabase.getCollection("finished");
        //
        Map<String,Integer> count = new HashMap<>();
        MongoClient mongoClient1 = new MongoClient("127.0.0.1",27017);
        MongoDatabase mongoDatabase1 = mongoClient1.getDatabase("file_upload_history");
        MongoCollection mongoCollection1 = mongoDatabase1.getCollection("upload_history");

        mongoCollection.find().limit(2000000).forEach((Block<Document>) doc->{

            String url = (String)doc.get("url");
            Integer num = count.get(url);
            if(num==null){
                count.put(url,1);
            }else{
                count.put(url,num+1);
            }
            url = url.substring(url.lastIndexOf("/")+1);
            Document filter = new Document();
            filter.put("path",url);
            boolean hasNext = mongoCollection1.find(filter).iterator().hasNext();
            if(hasNext){
                Document updateDocument = new Document();
                updateDocument.put("finished",1);
                Document updateOpt = new Document();
                updateOpt.put("$set",updateDocument);
                mongoCollection1.updateOne(filter,updateOpt);
                return;
            }
            Document document = new Document();
            document.put("path",url);
            document.put("full_path","\\\\PS2022XXOQOOCG\\e\\bitCars\\images17w\\"+url);
            document.put("status",-4);
            mongoCollection1.insertOne(document);
            System.out.println(url);

        });
        System.out.println(count.size());

    }
}
