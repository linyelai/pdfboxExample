package com.linseven.config;


import com.mongodb.client.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/6/8 11:27
 */
@Configuration
public class AppConfig {

//    @Bean
//    public MongoClient mongoClient() {
//        return MongoClients.create("mongodb://127.0.0.1:27027");
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        return new MongoTemplate(mongoClient(), "test");
//    }
}
