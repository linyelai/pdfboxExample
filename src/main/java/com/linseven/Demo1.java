package main.java.com.linseven;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2021/10/21 10:04
 */

public class Demo1 {


    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread( ()->{

            for(int i = 0;i<=10000000;i++){

                addByte("hello world".getBytes());
            }

        });
        thread.start();
        Thread.sleep(1000*10000);

    }

    public static void addByte(byte [] data){

        System.out.println(new String(data));

    }
}
