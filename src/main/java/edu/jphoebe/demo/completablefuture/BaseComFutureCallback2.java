package edu.jphoebe.demo.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BaseComFutureCallback2 {
    public static void main(String[] args) throws InterruptedException {
        // ExecutorService executor = Executors.newFixedThreadPool(1);

        CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("get start,will sleep 3s");
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Hello CompletableFuture";
        });

        /**
         * ps: 回调函数，顾名思义 那就是调用的对象中声明的方法正确执行完后，就会调用这个方法。
         * 其中，accept中的参数就是get函数返回的参数
         */
        System.out.println(resultCompletableFuture.thenAccept((t) -> {
            System.out.println("进入回调函数-" + t);
            System.out.println(Thread.currentThread().getName());
        }));

        System.out.println("带有回调的print语句后面一句话");
        System.out.println("");

        /**
         * it will shutdown 10's later
         */
        System.out.println("it will shutdown 10's later");
//		TimeUnit.SECONDS.sleep(3);

        // executor.shutdown();
    }
}
/**
 * ps： 这里我是强行让主线程睡十秒（大于模拟阻塞中的3秒），然后再往下执行（会关掉，没有语句了）
 */
