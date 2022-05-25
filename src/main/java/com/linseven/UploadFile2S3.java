package main.java.com.linseven;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.com.linseven.test.UploadTask;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.concurrent.*;


/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/5/18 11:27
 */
public class UploadFile2S3 {

    public static void main(String[] args) {

        ExecutorService executorService = new ThreadPoolExecutor(40,100,60*30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(2000000));
        MongoClient mongoClient = new MongoClient("127.0.0.1",27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("file_upload_history");
        MongoCollection mongoCollection = mongoDatabase.getCollection("upload_history");
        Document document = new Document();
        document.append("status",-3);
        Document sortDocument = new Document();
        sortDocument.append("_id",-1);
        mongoCollection.find(document).sort(sortDocument).limit(2000000).forEach((Block<Document>) doc->{

//            doc.put("status",3);
//            Document filter = new Document();
//            filter.put("path",doc.get("path"));
//            Document updateOperation = new Document();
//            updateOperation.put("$set",doc);
//            mongoCollection.updateOne(filter,updateOperation);
            executorService.submit(new UploadTask(doc,mongoCollection));
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });


    }
}
