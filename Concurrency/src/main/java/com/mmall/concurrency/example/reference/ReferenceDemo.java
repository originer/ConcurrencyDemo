package com.mmall.concurrency.example.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author Zz
 **/
public class ReferenceDemo {
    static Object object = new Object();


    public static void main(String[] args) {
//        testStrongReference();
//        testSoftReference();
//        testWeakReference();
        testPhantonReference();
    }

    /**
     * after system.gc-strongReference---obj = java.lang.Object@65b54208
     * 强引用只要还存在，对象就不会被回收
     */
    private static void testStrongReference() {
        Object obj = object;
        object = null;
        System.gc();
        System.out.print("after system.gc-strongReference---obj = " + obj);
    }

    /**
     * after system.gc---softReference = java.lang.ref.SoftReference@65b54208
     * 如果内存足够，在gc后也不会被回收，很适合用来做缓存
     */
    private static void testSoftReference() {
        SoftReference<Object> obj = new SoftReference<>(object);
        object = null;
        System.gc();
        System.out.print("after system.gc---softReference = " + obj);
    }


    /**
     * GC:
     * after system.gc---weakReference = null
     * after system.gc---weakReferenceStr = null
     * <p>
     * 在object，str设置为null后，如果发生gc，就get不到数据
     * 如果不发生gc还可以get到数据
     */
    private static void testWeakReference() {
        StringBuilder str = new StringBuilder("test");
        WeakReference<Object> weakReference = new WeakReference<Object>(object);
        WeakReference<Object> weakReferenceStr = new WeakReference<Object>(str);
        object = null;
//        str = null;
        System.gc();
        System.out.println("after system.gc---weakReference = " + weakReference.get());
        System.out.print("after system.gc---weakReferenceStr = " + weakReferenceStr.get());
    }

    /**
     * after system.gc---phantomReference = null
     * after system.gc---phantomReferenceStr = null
     * 无论是否发生GC get的数据都为null，可以看出，弱引用其实是没有任何效果的引用
     */
    private static void testPhantonReference() {
        StringBuilder str = new StringBuilder("test");
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(object, referenceQueue);
        PhantomReference<Object> phantomReferenceStr = new PhantomReference<>(str, referenceQueue);
        //        object = null;
        //        str = null;
        //        System.gc();
        System.out.println("after system.gc---phantomReference = " + phantomReference.get());
        System.out.print("after system.gc---phantomReferenceStr = " + phantomReferenceStr.get());
    }

}
