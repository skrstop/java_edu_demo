package edu.jphoebe.demo.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2020-04-29 18:30:16
 */
public class SimpleDemo {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class Text {
        private String msg;
    }

    public static void main(String[] args) throws InterruptedException {
        // 在父线程中设置
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<String>();
//        ThreadLocal<Text> context = new ThreadLocal<Text>();
//        InheritableThreadLocal<Text> context = new InheritableThreadLocal<Text>();

//        Text txt = Text.builder()
//                .msg("aaaaa")
//                .build();

        // 非线程池版
        // 在子线程中可以读取，值是"value-set-in-parent"
//        context.set("aaaaa");
//        new Thread(() -> {
//            for (int i = 0; i < 1000; i++) {
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                String value = context.get();
//                System.out.println(value);
//            }
//        }).start();
//
//        for (int i = 0; i < 1000; i++) {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            context.set(String.valueOf(i));
//        }
//
//        try {
//            TimeUnit.SECONDS.sleep(3);
////            Text value = context.get();
////            System.out.println(value.getMsg());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 线程池版
        ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));
        context.set("aaaa");
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " || " + context.get());
        });
        TimeUnit.SECONDS.sleep(1);
        context.set("aaaa1");
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " || " + context.get());
        });
        TimeUnit.SECONDS.sleep(1);
        context.set("aaaa2");
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " || " + context.get());
        });

    }

}
