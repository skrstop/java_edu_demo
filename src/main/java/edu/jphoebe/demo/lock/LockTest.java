package edu.jphoebe.demo.lock;


import java.util.concurrent.locks.ReentrantLock;

/**
 * LockTest class
 *
 * @author 蒋时华
 * @date 2017/09/22
 */
public class LockTest {


    public static void main(String[] args) {

        LockTest lockTest = new LockTest();
        ReentrantLock lock = new ReentrantLock();


        new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(5000);
                lockTest.print("lock");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            final ReentrantLock lock2 = lock;
            try {
                lock2.lock();
                lockTest.print("lock2");
            } finally {
                lock2.unlock();
            }
        }).start();

    }

    public void print(String msg) {
        System.out.println("父类正在执行" + msg);
    }

}
