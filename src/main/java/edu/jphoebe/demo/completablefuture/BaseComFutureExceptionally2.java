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
public class BaseComFutureExceptionally2 {

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
                    throw new RuntimeException("错误");
                    //			System.out.println(Thread.currentThread().getName());
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
                throw new RuntimeException("aaaaa");
//				System.out.println(Thread.currentThread().getName());
            }

        }).exceptionally(new Function<Throwable, Void>() {

            /**
             * ps
             * 当出现异常的的时候，会执行这个function
             * 这里就是对异常进行一些处理
             */
            @Override
            public Void apply(Throwable t) {
                System.out.println(t.getMessage());
                return null;
            }
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

        System.out.println("time out ,main shutdown now");
    }

}
/**
 * 我们往往不仅需要通过回调的方式，让线程不阻塞
 * 我们还需要将这些回调操作串起来
 * 接着请看下面的转换
 * 需要用到apply函数组
 * <p>
 * <p>
 * public <U> CompletableFuture<U> 	thenApply(Function<? super T,? extends U> fn)
 * public <U> CompletableFuture<U> 	thenApplyAsync(Function<? super T,? extends U> fn)
 * public <U> CompletableFuture<U> 	thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
 * 当原来的CompletableFuture计算完后，
 * 将结果传递给函数fn，
 * 将fn的结果作为新的CompletableFuture的参数去参与运算。
 * 因此它的功能相当于将CompletableFuture<T>转换成CompletableFuture<U>
 * <p>
 * 持续这么循环的调下去，有点像递归，当然，递归是本函数的持续调用（直到递归条件不满足）
 */
