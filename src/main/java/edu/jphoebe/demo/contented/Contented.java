package edu.jphoebe.demo.contented;


//import jdk.internal.vm.annotation.Contended;

import java.util.concurrent.CountDownLatch;

/**
 * @author 蒋时华
 * @date 2022-08-19 09:42:25
 */
public class Contented {

    public static class NoCacheLineFill {
//        public volatile long x;
//        public volatile long y;

//        public volatile long x;
//        public volatile long a, b, c, d, e, f, g;
//        public volatile long h, i, j, k, l, m, n;
//        public volatile long h1, i2, j3, k4, l5, m6, n7;
//        public volatile long y;


        //        @Contended
        public volatile long x;
        //        @Contended
        public volatile long y;

    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(2);
        NoCacheLineFill[] arr = new NoCacheLineFill[2];
        arr[0] = new NoCacheLineFill();
        arr[1] = new NoCacheLineFill();
        Thread threadA = new Thread(() -> {
            for (long i = 0; i < 1_000_000_000L; i++) {
                arr[0].x = i;
            }
            countDownLatch.countDown();
        }, "ThreadA");
        Thread threadB = new Thread(() -> {
            for (long i = 0; i < 100_000_000L; i++) {
                arr[1].y = i;
            }
            countDownLatch.countDown();
        }, "ThreadB");
        final long start = System.nanoTime();
        threadA.start();
        threadB.start();

        countDownLatch.await();
        final long end = System.nanoTime();
        System.out.println("耗时：" + (end - start) / 1_000_000 + "毫秒");
    }
}
