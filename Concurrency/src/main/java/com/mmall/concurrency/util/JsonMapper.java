package com.mmall.concurrency.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // config
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    public static <T> String obj2String(T src) {
        if (src == null) {
            return null;
        }
        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("parse object to String exception, error:{}", e);
            return null;
        }
    }

    public static <T> T string2Obj(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, typeReference));
        } catch (Exception e) {
            log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, error:{}", src, typeReference.getType(), e);
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Map map = new HashMap();
        map.put("tets", "t");
        map.put("tets1", "t");
        map.put("tets2", "t");
        map.put("tets3", "t");

        //模拟多线程解析
        ExecutorService exe = Executors.newFixedThreadPool(9);
        long startTime = System.currentTimeMillis();
        final CountDownLatch c = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            exe.execute(() -> {
                String s = JsonMapper.obj2String(map);
                log.info("解析次数："+c.getCount());
                c.countDown();
            });
        }
        c.await();
        //for (int i = 0; i < 2*COUNT; i++) {
        //    String s = JsonMapper.obj2String(map);
        //}
        long endTime = System.currentTimeMillis();
        System.out.println("obj2String耗时：" + (endTime - startTime));
    }
}
