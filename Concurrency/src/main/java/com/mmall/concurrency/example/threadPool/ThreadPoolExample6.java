package com.mmall.concurrency.example.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolExample6 {

    static int COUNT = 10000;
    public static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1000);

    /**
     * 1W  采用单个线程  耗时：11833
     * 1W  采用缓存的线程池  耗时：1886  启动的线程会过多
     * 1W  采用5个线程的固定线程池  耗时：2340
     * 1W  采用CPU核心+1 线程的固定线程池  耗时：1595
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        start();
        log.info("线程池处理结束");
    }

    static void start() throws InterruptedException {
        int availProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService singlePool = Executors.newSingleThreadExecutor();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ExecutorService fixPool = Executors.newFixedThreadPool(availProcessors+1);

        System.out.println("电脑可用核数："+availProcessors);
        final CountDownLatch countDownLatch = new CountDownLatch(10000);

        long start = System.currentTimeMillis();
        executorService.execute(() -> {
            for (int i = 0; i < COUNT; i++) {
                try {
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        /*缓存线程池*/
        /*改为2ms后  1W 耗时：646*/
        while (countDownLatch.getCount() != 0) {
            cachedThreadPool.execute(() -> {
                try {
                    Integer in = queue.take();
                    countDownLatch.countDown();
                    /*停止1ms 模拟事务处理*/
                    Thread.sleep(1);
                    log.info(Thread.currentThread().getName() + "处理" + in + "  剩余数量：" + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        //singlePool.execute(() -> {
        //    while (countDownLatch.getCount() != 0) {
        //        try {
        //            Integer in = queue.take();
        //            cachedThreadPool.execute(() -> {
        //                countDownLatch.countDown();
        //                /*停止1ms 模拟事务处理*/
        //                try {
        //                    Thread.sleep(2);
        //                } catch (InterruptedException e) {
        //                    e.printStackTrace();
        //                }
        //                log.info(Thread.currentThread().getName() + "处理" + in + "  剩余数量：" + queue.size());
        //
        //            });
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //});

        //for (int i = 0; i < availProcessors; i++) {
        //    fixPool.execute(() -> {
        //        while (countDownLatch.getCount() != 0) {
        //            try {
        //                Integer in = queue.take();
        //                countDownLatch.countDown();
        //                /*停止1ms 模拟事务处理*/
        //                Thread.sleep(2);
        //                log.info(Thread.currentThread().getName() + "处理" + in + "  剩余数量：" + queue.size());
        //            } catch (InterruptedException e) {
        //                e.printStackTrace();
        //            }
        //        }
        //    });
        //}

        //TimeUnit.SECONDS.sleep(8);
        //flag = false;
        countDownLatch.await();
        long end = System.currentTimeMillis();

        executorService.shutdown();
        System.out.println("耗时：" + (end - start));
    }
}
