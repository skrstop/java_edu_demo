package edu.jphoebe.demo.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


/***
 * 演示异常通知的情况
 * @author joy lee
 *
 */
public class BaseComFutureExceptionally {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        /*
         * 新建一个CompletableFuture对象
         */
        CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    System.out.println("get start,will sleep 3s");
                    TimeUnit.SECONDS.sleep(3);
                    //			throw new RuntimeErrorException(new Error("错误"));
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return "Hello CompletableFuture";
            }
        }, executor);

        System.out.println(resultCompletableFuture.thenAccept(new Consumer<String>() {
            @Override
            public void accept(String t) {
                System.out.println("进入回调函数-" + t);
                System.out.println(Thread.currentThread().getName());
            }

        }).exceptionally(new Function<Throwable, Void>() {

            @Override
            public Void apply(Throwable t) {
                System.out.println(t.getMessage());
                return null;
            }
            /**
             * ps
             * 当出现异常的的时候，会执行这个function
             * 这里就是对异常进行一些处理
             *
             * 但是我们实际应用中很定不是通过
             * resultCompletableFuture.completeExceptionally(new Exception("error"));
             * 这个方法来抛出异常，肯定是在get中执行的时候出现异常然后抛出
             * 这种情况详见BaseComFutureExceptionally2
             */
        }));

        resultCompletableFuture.completeExceptionally(new Exception("error"));

        System.out.println("it will shutdown 10's later");
        TimeUnit.SECONDS.sleep(10);

        executor.shutdown();
    }

}
