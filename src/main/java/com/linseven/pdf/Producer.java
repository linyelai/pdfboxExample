package main.java.com.linseven.pdf;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2020/11/23 19:22
 */
public class Producer {

    private static String topic = "mytopic";
    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers","localhost:9092");
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
       for(int i=0;i<1000;i++){

            ProducerRecord<String,String> record = new ProducerRecord<String,String>(topic,"key","value"+System.currentTimeMillis());
            producer.send(record);
            Thread.sleep(1000);

        }
        producer.close();

    }
}
