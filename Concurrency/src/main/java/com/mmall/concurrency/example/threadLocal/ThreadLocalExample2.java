package com.mmall.concurrency.example.threadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal 内存泄漏
 * @author Zz
 **/
public class ThreadLocalExample2 {
    public static class MyThreadLocal extends ThreadLocal {
        private byte[] a = new byte[1024*1024*1];

        @Override
        public void finalize() {
            System.out.println("My threadlocal 1 MB finalized.");
        }
    }

    public static class My50MB {//占用内存的大对象
        private byte[] a = new byte[1024*1024*50];

        @Override
        public void finalize() {
            //发生GC后调用
            System.out.println("My 50 MB finalized.");
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exe = Executors.newSingleThreadExecutor();

        exe.submit(()->{
            ThreadLocal tl = new MyThreadLocal();
            tl.set(new My50MB());

            tl=null;//断开ThreadLocal的强引用
            System.out.println("Full GC 1");
            System.gc();
        });


        System.out.println("关闭线程池");
        //如果线程池没被关闭，无论GC多少次My50MB都不会被回收，就发生了内存泄漏
        exe.shutdown();

        System.out.println("Full GC 2");
        System.gc();
        Thread.sleep(1000);
        System.out.println("Full GC 3");
        System.gc();
        Thread.sleep(1000);
        System.out.println("Full GC 4");
        System.gc();
        Thread.sleep(1000);
    }
}
