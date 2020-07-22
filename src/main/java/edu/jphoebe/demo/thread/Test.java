package edu.jphoebe.demo.thread;

import java.util.concurrent.CyclicBarrier;

class Test {


    public static void main(String[] args) {
        System.out.println(3 * 0.1 == 0.3);
        System.out.println(0.3 * 1 == 0.3);
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//
//        Test test = new Test();
//
//        executorService.execute(()->{
//            test.printA();6
//        });
//
//        executorService.execute(()->{
//            test.printB();
//        });
//
//        executorService.execute(()->{
//            test.printC();
//        });

    }

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    private void printB() {
        for (int i = 0; i < 100; i++) {
            System.out.println("AAAA-" + i);
        }
    }

    private void printC() {
        for (int i = 0; i < 100; i++) {
            System.out.println("BBBB-" + i);
        }
    }

    private void printA() {
        for (int i = 0; i < 100; i++) {
            System.out.println("CCCC-" + i);
        }
    }


}
