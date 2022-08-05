package edu.jphoebe.demo.algorithm.sort.quickSort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-05 10:00:03
 */
public class QuickSort {

    public static void main(String[] args) {
        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        quickSort(array, 0, array.length);

        System.out.println("排序后：" + JSONUtil.toJsonStr(array));
    }

    private static void quickSort(Integer[] array, int l, int r) {
        int first = l, end = r - 1, base = array[l];
        while (first < end) {
            // 大的向右
            while (first < end && array[end] >= base) {
                end--;
            }
            array[first] = array[end];
            // 小的向前
            while (first < end && array[first] <= base) {
                first++;
            }
            array[end] = array[first];
        }
        array[first] = base;
        if (l < first) {
            quickSort(array, l, first);
        }
        if (first + 1 < r) {
            quickSort(array, first + 1, r);
        }
    }

}
