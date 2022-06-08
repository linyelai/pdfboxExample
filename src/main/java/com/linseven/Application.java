package com.linseven;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/6/8 11:11
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.linseven.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
