package com.mmall.concurrency.example.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolExample2 {

    public static BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) {


        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        ExecutorService exc = Executors.newFixedThreadPool(1);

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(() -> {
                boolean flag = false;
                try {
                    queue.put(String.valueOf(index));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                if (flag)
                    log.info("{} 放入:{}  size:{}", Thread.currentThread().getName(),index, queue.size());
                if (queue.size() >= 5) {
                    log.info("queue阻塞");
                }
            });
            executorService.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.poll();
                log.info("{} 取出:{}",Thread.currentThread().getName(), index);

            });
        }
        executorService.shutdown();
    }
}
