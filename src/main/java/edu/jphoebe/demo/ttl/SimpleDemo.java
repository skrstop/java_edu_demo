package edu.jphoebe.demo.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2020-04-29 18:30:16
 */
public class SimpleDemo {

    public static void main(String[] args) {
        // 在父线程中设置
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<String>();
//        ThreadLocal<String> context = new ThreadLocal<String>();
//        InheritableThreadLocal<String> context = new InheritableThreadLocal<String>();
        context.set("value-set-in-parent");

        // 非线程池版
        // 在子线程中可以读取，值是"value-set-in-parent"
        new Thread(() -> {
            String value = context.get();
            System.out.println(value);
            context.set("value-set-in-parent1111111");
        }).start();

        try {
            TimeUnit.SECONDS.sleep(3);
            String value = context.get();
            System.out.println(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 线程池版
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 额外的处理，生成修饰了的对象executorService
        executorService = TtlExecutors.getTtlExecutorService(executorService);
        context.set("value-set-in-parent");
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " || " + context.get());
            }
        };
        executorService.submit(task);
        context.set("value-set-in-parent111");
        try {
            TimeUnit.SECONDS.sleep(3);
            String value = context.get();
            System.out.println(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " || " + context.get());
            }
        };
        executorService.submit(task);
    }

}
