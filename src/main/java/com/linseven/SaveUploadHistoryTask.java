package com.linseven;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOneModel;
import lombok.Synchronized;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/5/26 9:39
 */
public class SaveUploadHistoryTask  extends Thread
{

    private List<ReplaceOneModel> uploadHistoryList = new ArrayList<>();
    private boolean finish = false;
    private  MongoCollection mongoCollection;

    private static Object lock = new Object();

    public  SaveUploadHistoryTask(MongoCollection mongoCollection){
        this.mongoCollection = mongoCollection;
    }
    @Override
    public void run() {




            while (!finish) {


                if (uploadHistoryList.size() >= 100) {
                    synchronized (SaveUploadHistoryTask.class) {
                        mongoCollection.bulkWrite(uploadHistoryList);
                        uploadHistoryList.clear();
                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        }

    }


    public  void add(Document document){

        synchronized (SaveUploadHistoryTask.class) {
            Document filter = new Document();
            filter.put("_id", document.get("_id"));
            ReplaceOneModel updateOneOperator = new ReplaceOneModel(filter, document);
            uploadHistoryList.add(updateOneOperator);
        }
        if(uploadHistoryList.size()>100){
            try {
                synchronized (lock){
                    Thread.sleep(50);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
