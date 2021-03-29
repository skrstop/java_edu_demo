package edu.jphoebe.demo.forkjoin;

import java.util.concurrent.*;

/**
 * 要想使用Fark—Join，类必须继承
 * RecursiveAction（无返回值）
 * RecursiveTask（有返回值）
 */
public class ForkJoin2 extends RecursiveTask<Long> {
    // 每个"小任务"最多只打印50个数
    private static final long MAX = 50;

    private int start;
    private int end;

    ForkJoin2(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // 当end-start的值小于MAX时候，开始打印
        if ((end - start) < MAX) {
            long result = 0;
            for (int i = start + 1; i <= end; i++) {
                result += i;
            }
            return result;
        }
        // 将大任务分解成两个小任务
        int middle = (start + end) / 2;
        ForkJoin2 left = new ForkJoin2(start, middle);
        ForkJoin2 right = new ForkJoin2(middle, end);
        // 并行执行两个小任务
        left.fork();
        right.fork();
        return left.join() + right.join();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        // 提交可分解的PrintTask任务
        final ForkJoinTask<Long> submit = forkJoinPool.submit(new ForkJoin2(0, 200));
        // 阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);
        // 关闭线程池
        forkJoinPool.shutdown();
        // 结果输出
        System.out.println(submit.get());
    }
}
