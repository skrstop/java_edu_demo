package edu.jphoebe.demo.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 蒋时华
 * @date 2023-07-18 10:55:10
 */
public class VirtualThread {

    public static void main(String[] args) {

        ExecutorService virtualThreadPerTaskExecutor = Executors.newVirtualThreadPerTaskExecutor();

        long l1 = System.currentTimeMillis();
        int platformThreadCount = 10000;
        for (int i = 0; i < platformThreadCount; i++) {
            int finalI = i;
            Thread.ofPlatform().name("平台线程").start(() -> {
                String text = "线程名称：" + Thread.currentThread().getName() + "线程ID" + Thread.currentThread().threadId() + "执行第" + finalI + "个平台线程";
                String text2 = """
                        aaaa
                        """;
//                System.out.println();
            });
        }
        System.out.println(platformThreadCount + " 个平台线程执行耗时：" + (System.currentTimeMillis() - l1) + "毫秒");

        long l2 = System.currentTimeMillis();
        int virtualThreadCount = 10000;
        for (int i = 0; i < virtualThreadCount; i++) {
            int finalI = i;
            Thread.ofVirtual().name("虚拟线程").start(() -> {
                String text = "线程名称：" + Thread.currentThread().getName() + "线程ID" + Thread.currentThread().threadId() + "执行第" + finalI + "个虚拟线程";
                String text2 = """
                        bbb
                        """;
            });
        }
        System.out.println(virtualThreadCount + " 个虚拟线程执行耗时：" + (System.currentTimeMillis() - l2) + "毫秒");

    }

}
