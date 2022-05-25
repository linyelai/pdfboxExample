package main.java.com.linseven;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/5/19 9:28
 */
public class HandleFileName {

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient("127.0.0.1",27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("file_upload_history");
        MongoCollection mongoCollection = mongoDatabase.getCollection("upload_history");
        Document document = new Document();
        document.append("status",0);
        Document sortDocument = new Document();
        sortDocument.append("_id",1);
        AtomicInteger match = new AtomicInteger();
        mongoCollection.find(document).sort(sortDocument).limit(2000000).forEach((Block<Document>) doc->{


           Document filter = new Document();
            filter.put("path",doc.get("path"));
            filter.put("status",3);
            Document result = (Document) mongoCollection.findOneAndDelete(filter);
            if(result!=null)
            {
                System.out.println("match:"+result.get("path")+",num:"+match);
                match.getAndIncrement();

            }
           /// System.out.println("not match:"+doc.get("path"));

          /*  String originPath = (String)doc.get("path");
            filter.put("path",originPath);
            Document updateOperation = new Document();
            String path = (String)doc.get("path");
            path = path.substring(path.lastIndexOf("\\")+1);
            //doc.put("path",path);
            doc.put("status",0);
            System.out.println(path);
            updateOperation.put("$set",doc);
            mongoCollection.updateOne(filter,updateOperation);*/


        });


    }
}
