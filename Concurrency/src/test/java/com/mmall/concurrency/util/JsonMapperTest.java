package com.mmall.concurrency.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Horizon on 2018/8/30.
 */
@Slf4j
public class JsonMapperTest {

    public static int COUNT = 10000;

    @Test
    public void obj2String() throws InterruptedException {

        Map map = new HashMap();
        map.put("tets", "t");
        map.put("tets1", "t");
        map.put("tets2", "t");
        map.put("tets3", "t");

        //模拟多线程解析
        ExecutorService exe = Executors.newFixedThreadPool(9);
        long startTime = System.currentTimeMillis();
        final CountDownLatch c = new CountDownLatch(COUNT);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < COUNT; i++) {
            exe.execute(() -> {
                String s = JsonMapper.obj2String(map);
                atomicInteger.getAndIncrement();
                log.info("解析次数：" + atomicInteger.get());
                c.countDown();
            });
        }
        c.await();
        //for (int i = 0; i < 2*COUNT; i++) {
        //    String s = JsonMapper.obj2String(map);
        //}
        long endTime = System.currentTimeMillis();
        System.out.println("obj2String耗时：" + (endTime - startTime) + "tt:" + atomicInteger);
    }

    @Test
    public void string2Obj() {
    }
}