package edu.jphoebe.demo.algorithm.sort.selectionSort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-02 10:48:39
 */
public class SelectionSort {

    public static void main(String[] args) {

        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        for (int i = 0; i < array.length; i++) {

            int temp = array[i];
            int minIndex = i;
            // n-i
            for (int i1 = i; i1 < array.length; i1++) {
                if (array[minIndex] <= array[i1]) {
                    continue;
                }
                minIndex = i1;
            }
            if (i != minIndex) {
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }

        }

        System.out.println("排序后：" + JSONUtil.toJsonStr(array));


    }

}
