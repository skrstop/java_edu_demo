package edu.jphoebe.demo.lock;

import cn.auntec.framework.components.util.executor.ThreadPoolUtils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2021-03-29 14:04:28
 */
public class SemaphoreTest {

    public static void simple() {
        int thread = 10;
        final ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.newFixedThreadPool(thread);

        Semaphore semaphore = new Semaphore(thread / 2);

        for (int i = 0; i < thread; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(finalI + "。。。。。。准备工作");

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(finalI + "完成工作");
                semaphore.release();
            });
        }

    }

    public static void main(String[] args) {
        SemaphoreTest.simple();
    }

}
