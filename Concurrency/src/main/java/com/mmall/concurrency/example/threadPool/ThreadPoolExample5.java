package com.mmall.concurrency.example.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadPoolExample5 {

    private static AtomicInteger ai = new AtomicInteger(0);
    private static final BlockingQueue<String> blockQueue = new ArrayBlockingQueue<>(1000);

    public static void main(String[] args) throws Exception {
        ExecutorService ex = Executors.newCachedThreadPool();
        long s = System.currentTimeMillis();
        int count = 1000;
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        final Semaphore semaphore = new Semaphore(1);
        System.out.println(countDownLatch.getCount());
        ex.execute(() -> {
            for (int i = 0; i < count; i++) {
                blockQueue.offer("Index " + i);
                System.out.println("queue size:" + blockQueue.size() + "put: Index" + i);
                try {
                    TimeUnit.MICROSECONDS.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ex.execute(() -> {
            while (ai.get() != 1000) {
                String str = null;
                try {
                    semaphore.acquire();
//                    str = blockQueue.take();
                    str = blockQueue.poll();
                    if (str != null) {
                        ai.getAndIncrement();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();
                if (str != null) {
                    System.out.println(Thread.currentThread().getName() + ":" + str + "ai:" + ai.get());
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + ":线程休息中！");
                }
            }
        });

//        ex.execute(()->{
//            while(true) {
//                try {
//                    String str = blockQueue.take();
////                    String str = blockQueue.poll();
//                    if (str!=null) {
//                        System.out.println(Thread.currentThread().getName() + ":" + str + "ai:" + ai.get());
//                    } else {
//                        Thread.sleep(100);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        for (int i = 0; i < 8; i++) {
            ex.execute(() -> {
                while (true) {
                    if (ai.get() % 2 == 0) {
                        try {
                            System.out.println(Thread.currentThread().getName()+":线程开始休息！");
                            TimeUnit.SECONDS.sleep(1);
                            System.out.println(Thread.currentThread().getName()+"线程休息1S");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - s));
//        ex.shutdown();
    }
}
