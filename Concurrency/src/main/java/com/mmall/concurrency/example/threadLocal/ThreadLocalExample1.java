package com.mmall.concurrency.example.threadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zz
 **/
public class ThreadLocalExample1 {

    public static final ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "初始值");

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " 设置值为：" + Thread.currentThread().getName());
                threadLocal.set(Thread.currentThread().getName());
            });
        }

        TimeUnit.SECONDS.sleep(2);

        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "获得值:" + threadLocal.get());
            });
        }
        executor.shutdown();
        System.out.println(threadLocal.get());
    }
}
