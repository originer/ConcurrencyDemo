package com.mmall.concurrency.example.threadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zz
 **/
public class ThreadLocalExample3 {

//    public   int threadLocal = 0;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        final StringBuilder sb = new StringBuilder();


        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
//                System.out.println(Thread.currentThread().getName() + " 设置值为：" + Thread.currentThread().getName());
//                threadLocal.set(Thread.currentThread().getName());
                sb.append(Thread.currentThread().getName() + "\u0001");
            }).isDone();
        }

        TimeUnit.SECONDS.sleep(1);

        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "获得值:" + sb);
            });
        }
        executor.shutdown();
    }
}
