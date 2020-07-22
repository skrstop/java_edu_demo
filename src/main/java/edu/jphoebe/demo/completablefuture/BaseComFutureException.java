package edu.jphoebe.demo.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/***
 * 演示异常通知的情况
 * @author joy lee
 *
 */
public class BaseComFutureException {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        /*
         * 新建一个CompletableFuture对象
         */
        CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("get start,will sleep 3s");
                TimeUnit.SECONDS.sleep(3);
                if (true) {
                    throw new RuntimeException("异步执行报错了");
                }
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Hello CompletableFuture";
        }, executor);

        System.out.println(resultCompletableFuture.thenAccept((t) -> {
            System.out.println("进入回调函数-" + t);
            System.out.println(Thread.currentThread().getName());
        }));

        /**
         * ps: 我们返现，其余的流程都是一样的，就是回调函数不再执行了！
         * 任务还是会执行完成（只要主线程等待足够时间再结束，或者不结束 ）
         * 但是，我们的回调函数，一般应用中肯定是希望执行的（既然是回调，我肯定希望在目标方法执行完后，进行一些处理工作），哪
         * 怕是你报错，我也要指导你报错了，你完全不执行，显然是不合理的
         *
         * 但是我们实际应用中坑定不是通过
         * resultCompletableFuture.completeExceptionally(new Exception("error"));
         * 这个方法来抛出异常，肯定是在get中执行的时候出现异常然后抛出
         * 这种情况详见BaseComFutureExceptionally2
         */
        resultCompletableFuture.completeExceptionally(new RuntimeException("error"));

        System.out.println("it will shutdown 10's later");
        TimeUnit.SECONDS.sleep(10);

        executor.shutdown();
    }

}
