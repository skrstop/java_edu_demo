package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-04 09:49:31
 */
public class InsertSort {

    public static void main(String[] args) {


        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        boolean swap = false;
        for (int i = 1; i < size; i++) {

            int temp = array[i];
            int i1;
            for (i1 = i; i1 > 0 && temp < array[i1 - 1]; i1--) {
                array[i1] = array[i1 - 1];
            }
            if (i1 != i) {
                array[i1] = temp;
            }
        }
        System.out.println("排序前：" + JSONUtil.toJsonStr(array));


    }

}
