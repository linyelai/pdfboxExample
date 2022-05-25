package main.java.com.linseven;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2021/11/1 21:43
 */
public class ThreadLession {

    public static void main(String[] args) throws InterruptedException {


        for(int i=0;i<10;i++){
            Thread thread = new Thread(()->{
                System.out.println("hello world");
            });
            thread.start();
        }
        Thread.sleep(1000*60*50);
    }
}
