package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-03 11:01:45
 */
public class RadixSort {

    public static void main(String[] args) {


        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));
        // 获取最大值
        int max = array[0];
        for (int i = 1; i < size; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        int div = 1;
        while (max > 0) {
            array = countSort(array, div);
            div *= 10;
            max /= 10;
        }

        System.out.println("排序后：" + JSONUtil.toJsonStr(array));
    }

    public static Integer[] countSort(Integer[] array, int div) {
        int[] bucket = new int[10];

        // 找到最小值
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        // 确定数量
        for (int i = 0; i < array.length; i++) {
            int val = array[i] / div % 10;
            bucket[val]++;
        }
        // 确定下标
        for (int i = 1; i < bucket.length; i++) {
            bucket[i] += bucket[i - 1];
        }
        // 排序
        Integer[] sortArray = new Integer[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            int val = array[i] / div % 10;
            int sort = bucket[val];
            bucket[val]--;
            sortArray[sort - 1] = array[i];
        }
        return sortArray;
    }

}
