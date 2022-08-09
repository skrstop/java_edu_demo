package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-04 09:49:31
 */
public class HeapSort {

    public static void main(String[] args) {
        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        // 从下至上构建大顶堆
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapSort(array, i, size);
        }
        // 从上之下获取最大值
        for (int i = size - 1; i > 0; i--) {
            int tmp = array[0];
            array[0] = array[i];
            array[i] = tmp;
            // 将最小值下沉
            heapSort(array, 0, i);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));
    }

    public static void heapSort(Integer[] array, int start, int end) {
        int l = 2 * start + 1, r = l + 1, maxIndex = start;
        if (l < end && array[l] > array[maxIndex]) {
            maxIndex = l;
        }
        if (r < end && array[r] > array[maxIndex]) {
            maxIndex = r;
        }
        if (start != maxIndex) {
            int tmp = array[maxIndex];
            array[maxIndex] = array[start];
            array[start] = tmp;
            // 发生了交换，需要把子级也处理了
            heapSort(array, maxIndex, end);
        }
    }

}
