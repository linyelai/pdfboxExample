package com.linseven;

import com.linseven.test.UploadTask;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/5/26 18:16
 */
public class UploadThread extends  Thread {

    @Override
    public void run() {

        ExecutorService executorService = new ThreadPoolExecutor(40,50,60*30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(2000000));
        MongoClient mongoClient = new MongoClient("127.0.0.1",27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("file_upload_history");
        MongoCollection mongoCollection = mongoDatabase.getCollection("upload_history");
        Document document = new Document();
        document.append("status",-4);
        Document sortDocument = new Document();
        sortDocument.append("_id",1);
        SaveUploadHistoryTask saveWorker = new SaveUploadHistoryTask(mongoCollection);
        saveWorker.start();
        mongoCollection.find(document).sort(sortDocument).limit(1140000).forEach((Block<Document>) doc->{

//            doc.put("status",3);
//            Document filter = new Document();
//            filter.put("path",doc.get("path"));
//            Document updateOperation = new Document();
//            updateOperation.put("$set",doc);
//            mongoCollection.updateOne(filter,updateOperation);
            executorService.submit(new UploadTask(doc,mongoCollection,saveWorker));
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        });

    }
}