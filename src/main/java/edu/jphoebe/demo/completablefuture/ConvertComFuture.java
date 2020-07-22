package edu.jphoebe.demo.completablefuture;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 转换
 * <p>
 * 我们不仅仅回调，还可以将这些回调 操作串起来
 * <p>
 * 需要用到apply函数组
 *
 * @author joy lee
 */
public class ConvertComFuture {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
//			throw new RuntimeException("错误");
            System.out.println("aaaaa");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "zero";
        }, executor);

        CompletableFuture<Integer> f2 = f1.thenApply(new Function<String, Integer>() {

            @Override
            public Integer apply(String t) {
                System.out.println("进入f2的apply方法");
                System.out.println("f1传进来的字符串-" + t);
                System.out.println("返回该字符串的长度-" + Integer.valueOf(t.length()));

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Integer.valueOf(t.length());
            }
        }).exceptionally(new Function<Throwable, Integer>() {

            @Override
            public Integer apply(Throwable t) {
                System.out.println(t.getMessage());
                return null;
            }

        });
        System.out.println("bbbbb");

        /**
         * 此处，apply的是f1
         */

        CompletableFuture<Double> f3 = f2.thenApply(self -> self * 2.0).thenApply(self -> self * 3).exceptionally(new Function<Throwable, Double>() {
            @Override
            public Double apply(Throwable t) {
                System.out.println(t.getMessage());
                return null;
            }
        });//这个参数名字随便取，叫self最合适,因为他本来就是把f2自身的结果带到f3中去,参与f3的运算
        /**
         * 此处，apply的是f2
         *
         * 这个地方的self，不严格的说，就是一个的代名词,就是代表了f2的返回值
         *
         * 如果想要看的清楚一点，可以像f1一样显式声明一个function匿名对象，覆盖apply方法，然后写逻辑
         * 其中，方括号前面的参数是传进来的参数类型，后面的参数类型是返回类型
         *
         * 不过,我们一般采用f2的方式，更简洁一点
         *
         * 这里需要注意的是，f3获得最终结果还真不会马上执行，也不会导致主进程阻塞
         * 而是等着这里面所有的“回调”阶段一个接一个的完成后，再显示出来(或者说再进入f3的执行执行逻辑)。
         *
         * 但是，这个方法一旦有异常，就会抛出
         */
        //System.out.println(f3.get());


        System.out.println("shutdown in 3s");
        TimeUnit.SECONDS.sleep(3);

        System.out.println("shutdown");
        executor.shutdown();
    }

}
