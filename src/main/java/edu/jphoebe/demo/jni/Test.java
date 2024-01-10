package edu.jphoebe.demo.jni;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2022-08-02 14:30:23
 */
public class Test {


    static {
        // 平台类库目录下
//        System.loadLibrary("JniDemoImpl");
        // 自定义
//        System.load("/Users/jphoebe/opt/code/IdeaProjects/edu/java_edu_demo/src/main/java/edu/jphoebe/demo/jni/cSource/demo/JniDemoImpl_x86.jnilib");
        System.load("/Users/jphoebe/opt/code/IdeaProjects/edu/java_edu_demo/src/main/java/edu/jphoebe/demo/jni/cSource/exception/JniExceptionImpl_arm.jnilib");
    }

    public static void main(String[] args) throws InterruptedException {
        // demo
//        String s = "111";
//        JniDemo.printByNative(s);
//        JniDemo jniDemo = new JniDemo() {
//            @Override
//            public void callback(String msg) {
//                super.callback(msg);
//                System.out.println("重写jni回调");
//            }
//        };
//        int add = jniDemo.add(1, 2);
//        System.out.println(add);
//        // 回调监听注册
//        jniDemo.setCallback();

        // 异常处理
        JniExceptionDemo jniException = new JniExceptionDemo();

        CountDownLatch countDownLatch = new CountDownLatch(1);

//        ThreadUtil.execute(() -> {
//            try {
////                countDownLatch.countDown();
//                System.out.println("除0：" + jniException.exception1());
//            } catch (Error | Exception e) {
//                System.out.println("出现异常");
//                e.printStackTrace();
//            }
//        });

        ThreadUtil.execute(() -> {
            try {
//                countDownLatch.countDown();
                System.out.println("空指针: " + jniException.exception2());
            } catch (Error | Exception e) {
                System.out.println("出现异常");
                e.printStackTrace();
            }
        });

//        ThreadUtil.execute(() -> {
//            try {
////                countDownLatch.countDown();
//                System.out.println("堆栈破坏：" + jniException.exception3());
//            } catch (Error | Exception e) {
//                System.out.println("出现异常");
//                e.printStackTrace();
//            }
//        });

        countDownLatch.await();
        while (true) {
            System.out.println("主线程执行-----");
            TimeUnit.SECONDS.sleep(1);
        }

    }


}
