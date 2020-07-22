package edu.jphoebe.demo.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * 要想使用Fark—Join，类必须继承
 * RecursiveAction（无返回值）
 * RecursiveTask（有返回值）
 */
public class ForkJoin extends RecursiveTask<Long> {
    @Override
    protected Long compute() {

        return null;
    }

    public static void main(String[] args) {
        System.out.println("aaa");
    }
}
