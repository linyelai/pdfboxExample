package com.linseven;

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
 * @date 2022/5/26 17:08
 */
public class InitData {
    public static void main(String[] args) {

       // while(true){

            InitDataThread thread= new InitDataThread();
            thread.start();
           try {
                Thread.sleep(1500*1000*60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
       // }
    }
}
