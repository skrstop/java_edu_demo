package edu.jphoebe.demo.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 演示回调
 *
 * @author Administrator
 */
public class BaseComFutureCallback {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        /*
         * 新建一个CompletableFuture对象
         */
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

        CompletableFuture<String> resultCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
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
        System.out.println(resultCompletableFuture.thenAccept((t) -> {
            System.out.println("进入回调函数-" + t);
            System.out.println(Thread.currentThread().getName());
        }));
        System.out.println(resultCompletableFuture2.thenAccept((t) -> {
            System.out.println("进入回调函数-" + t);
            System.out.println(Thread.currentThread().getName());
        }));

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
 * ps: 两点
 * 1.这个时候你会看到 ,在带有回调的print语句后的一个print会先打印（因为那个回调在等着get函数里面sleep3秒钟）,
 * 但是，不会阻塞着等，而是会执行下面的语句，然后，上面的语句执行完后，再来执行这个带有回调的print
 * 2.当打印线程的时候，我们可以看到，回调与get是统一个线程！
 * 因为我们用的thenAccept去做回调，
 * 那么，当我们尝试用带Async的回调方法去回调试试看
 * 详见BaseComFutureCallbackAsync
 * <p>
 * <p>
 * 补充：
 * 这里与上一个不同，因为这个地方加了个executor，为什么？
 * 由于回调，resultCompletableFuture中的get不知道会阻塞到什么时候
 * （虽然现在写死的是3秒，但是生产环境中 ，哪个能保证3秒？）
 * 但是，这个地方他又不会像上一个程序那样一顺执行，非得等阻塞完成再执行下一句
 * 而是马上执行下面的print，当main中所有语句顺序执行完了之后，这个main线程就关闭了，
 * 所以，你的回调永远不会执行，因为主线程都down掉了。你咋回来。（可惜啊，main不会等，等了不还是阻塞）
 * <p>
 * 所以，这里我先起一个executor，用来开一个线程池，如果不显式关闭，
 * 他就会一直挂在那，既然一直挂在那里，我显然，会等到我回调的那天（get必然会返回值，只是时间长短啊）
 * <p>
 * 所以，哪怕你main语句执行完了，但是我只要不关闭这个线程池，你的main就一直跟我挂在那，因为还有一句话没有执行完嘛.
 * <p>
 * BaseComFutureCallback2中对这个的演示会更明显
 */
