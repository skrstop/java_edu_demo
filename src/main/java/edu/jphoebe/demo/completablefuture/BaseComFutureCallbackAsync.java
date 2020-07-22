package edu.jphoebe.demo.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * thenAcceptAsync演示
 *
 * @author joy lee
 */
public class BaseComFutureCallbackAsync {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("get start,will sleep 3s");
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello CompletableFuture";
        }, executor);

        /**
         * ps: 回调函数，顾名思义 那就是调用的对象中声明的方法正确执行完后，就会调用这个方法。
         * 其中，accept中的参数就是get函数返回的参数
         */
        System.out.println(resultCompletableFuture.thenAcceptAsync((t) -> {
            System.out.println("进入回调函数-" + t);
            System.out.println(Thread.currentThread().getName());
        }, executor));
        /**
         * 有没有这个executor无所谓啊，也可以不要，这里只是把他放到我们新建的fix-pool中去
         * 如果没有，ForkJoinPool中的线程池了
         */

        System.out.println("带有回调的print语句后面一句话");
        System.out.println("");

        /**
         * it will shutdown 10's later
         */
        System.out.println("it will shutdown 10's later");
        TimeUnit.SECONDS.sleep(10);

        executor.shutdown();
        System.out.println("shutdown");
    }

}
/**
 * ps : 这里我们显示了如何用不同线程处理回调
 * <p>
 * 但是，当我们遇到异常了怎么办？
 * <p>
 * 请看下一个内容：BaseComFutureException
 */
