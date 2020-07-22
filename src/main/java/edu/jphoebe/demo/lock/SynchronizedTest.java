package edu.jphoebe.demo.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Lock1 class
 *
 * @author 蒋时华
 * @date 2017/09/22
 */
public class SynchronizedTest {

    Object classLock = SynchronizedTest.class;

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        executorService.execute(() -> {
            try {
                synchronizedTest.test("线程A");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 保证第一个线程先启动并执行
        Thread.sleep(1000);

        executorService.execute(() -> {
            try {
                synchronizedTest.test("线程B");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void test(String msg) throws InterruptedException {
        classLock = "aaaa";
        synchronized (classLock) {
            System.out.println(msg + " test start");
            Thread.sleep(5000);
            System.out.println(msg + " test end");
        }
    }

    public synchronized static void test2(String msg) {
        System.out.println(msg + " test222222 start");
        System.out.println(msg + " test222222 end");
    }

}
