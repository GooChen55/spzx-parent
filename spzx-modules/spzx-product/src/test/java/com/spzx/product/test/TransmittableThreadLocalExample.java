package com.spzx.product.test;

import com.alibaba.ttl.TransmittableThreadLocal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TransmittableThreadLocal 是阿里巴巴开源的一个 Java 库，它扩展了 ThreadLocal 类，使得在异步编程中可以传递线程局部变量。
public class TransmittableThreadLocalExample {
    // 创建一个 TransmittableThreadLocal 实例
    static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();
    //static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        // 设置线程局部变量的值
        THREAD_LOCAL.set(new ConcurrentHashMap<String, Object>()); // ConcurrentHashMap 线程安全的Map类
        THREAD_LOCAL.get().put("key", "111");

        // 在新线程中使用线程局部变量
        Thread t1 = new Thread(() -> {
            System.out.println("新线程中的值：" + THREAD_LOCAL.get().get("key"));
            THREAD_LOCAL.get().put("key","222");
        });
        t1.start();

        System.out.println("主线程中的值：" + THREAD_LOCAL.get().get("key"));

        t1.join();

        // 主线程中的值
        System.out.println("主线程中的值：" + THREAD_LOCAL.get().get("key"));
    }
}