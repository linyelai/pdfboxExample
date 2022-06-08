package com.linseven;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/5/26 11:41
 */
public class ImagesHanlder {

    public static void main(String[] args) throws IOException {

        ExecutorService executorService = new ThreadPoolExecutor(40,100,60*30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(2000000));
        MongoClient mongoClient = new MongoClient("127.0.0.1",27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("bidcars");
        MongoCollection mongoCollection = mongoDatabase.getCollection("vinDetail");
        Document document = new Document();
        document.append("status",-3);
        Document sortDocument = new Document();
        sortDocument.append("_id",-1);
        SaveUploadHistoryTask saveWorker = new SaveUploadHistoryTask(mongoCollection);
        saveWorker.start();
        List<String> vinCodeList = new ArrayList<>();
        mongoCollection.find().sort(sortDocument).limit(2000000).forEach((Block<Document>) doc->{

            vinCodeList.add((String)doc.get("vinCode"));
        });


        FileWriter fileWriter = new FileWriter("d:/vinCodeList.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for(String vinCode:vinCodeList){

            bufferedWriter.write(vinCode);
            bufferedWriter.newLine();

        }
        bufferedWriter.close();
        fileWriter.close();

    }
}
