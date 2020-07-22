package edu.jphoebe.demo.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 基本的CompletableFuter使用
 *
 * @author joy lee
 */
public class BaseComFuture {

    public static void main(String[] args) throws InterruptedException, ExecutionException {


        long start = System.nanoTime();
//
//		System.out.println("get start");
//		TimeUnit.SECONDS.sleep(3);
//		TimeUnit.SECONDS.sleep(3);
//		TimeUnit.SECONDS.sleep(3);
//		System.out.println(456);
//
//		System.out.println(System.nanoTime()-start);

        /*
         * 新建一个CompletableFuture对象
         */
        start = System.nanoTime();
        CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("get start1");
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Hello CompletableFuture";
        });
//		CompletableFuture<String> resultCompletableFuture1 = CompletableFuture.supplyAsync(()->{
//			try {
//				System.out.println("get start2");
//				TimeUnit.SECONDS.sleep(3);
//				System.out.println(Thread.currentThread().getName());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return "Hello CompletableFuture";
//		});
//		CompletableFuture<String> resultCompletableFuture2 = CompletableFuture.supplyAsync(()->{
//			try {
//				System.out.println("get start3");
//				TimeUnit.SECONDS.sleep(5);
//				System.out.println(Thread.currentThread().getName());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return "Hello CompletableFuture";
//		});
        TimeUnit.SECONDS.sleep(1);
        System.out.println(123);
        System.out.println(resultCompletableFuture.get());
        System.out.println("aaaaaaa");
//		System.out.println(resultCompletableFuture1.get());
        System.out.println("aaaaaaa");
//		System.out.println(resultCompletableFuture2.get());
        System.out.println("aaaaaaa");
        System.out.println(456);
        System.out.println(System.nanoTime() - start);
        /***
         * ps : 首先会进入该进程，执行get方法 //所以输出结果中立马会输出get start 然后，sleep模拟长时间计算操作/或者其他情况的阻塞
         * //所以这个时候屏幕会等三秒，一直停在那里 3秒后才会打印 // 三秒后，才会开始打印相关信息，按照顺序执行
         * (不要被try语句迷惑了啊，那就是一个一瞬执行下来的逻辑) 打印完后该进程退出！
         *
         * 如果我用resultCompletableFuture的回调函数去处理这个会有什么现象呢？ 请看BaseComFutureCallback类中的演示
         */

        /**
         * 上面那个打印语句会阻塞3秒，执行完后 ，才会执行这一句
         */

    }

}
