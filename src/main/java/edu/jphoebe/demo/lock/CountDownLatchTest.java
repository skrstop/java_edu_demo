package edu.jphoebe.demo.lock;

import cn.auntec.framework.components.util.executor.ThreadPoolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author 蒋时华
 * @date 2021-03-29 13:07:49
 */
public class CountDownLatchTest {

    public static void simple() {
        int thread = 10;
        final ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.newFixedThreadPool(thread);
        CountDownLatch countDownLatch = new CountDownLatch(thread);

        for (int i = 0; i < thread; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                System.out.println(finalI + "。。。。。。准备工作");
                countDownLatch.countDown();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(finalI + "完成工作");
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("全部准备完毕，开始工作: ");
    }

    public static void andFuture() {
        int thread = 10;
        final ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.newFixedThreadPool(thread);
        CountDownLatch countDownLatch = new CountDownLatch(thread);
        List<Future<Boolean>> results = new ArrayList<>();
        for (int i = 0; i < thread; i++) {
            results.add((Future<Boolean>) threadPoolExecutor.submit(() -> {
                countDownLatch.countDown();
                final int res = new Random().nextInt(10);
//                if (res > 4) {
//                    return false;
//                }
                return true;
            }));
        }

        System.out.println("校验结果是否全部正常");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean valid = true;
        for (Future<Boolean> future : results) {
            try {
                if (!valid) {
                    continue;
                }
                final Boolean suc = future.get();
                System.out.println("子任务结果： " + suc);
                if (!suc) {
                    valid = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                future.cancel(true);
            }

        }
        System.out.println("最终结果：" + valid);
        threadPoolExecutor.shutdown();
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ExecutionException, InterruptedException {


    }

}
