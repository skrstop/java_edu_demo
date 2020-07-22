package edu.jphoebe.demo.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2020-04-29 18:30:16
 */
public class SimpleDemo {

    public static void main(String[] args) {
        // 在父线程中设置
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<String>();
        context.set("value-set-in-parent");

        // 在子线程中可以读取，值是"value-set-in-parent"
        new Thread(() -> {
            String value = context.get();
            System.out.println(value);
        }).start();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
