package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-05 17:50:34
 */
public class MergeSort {

    public static void main(String[] args) {

        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }
        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        Integer[] tmp = new Integer[size];
        mergeSort(array, 0, size - 1, tmp);

        System.out.println("排序后：" + JSONUtil.toJsonStr(array));


    }

    public static void mergeSort(Integer[] nums, int start, int end, Integer[] tmp) {
        if (start >= end) {
            return;
        }
        int middle = (start + end) / 2;
        mergeSort(nums, start, middle, tmp);
        mergeSort(nums, middle + 1, end, tmp);
        merge(nums, start, end, middle, tmp);
    }

    public static void merge(Integer[] nums, int start, int end, int middle, Integer[] tmp) {
        int l = start, r = middle + 1, tmpIndex = 0;
        while (l <= middle && r <= end) {
            if (nums[l] >= nums[r]) {
                tmp[tmpIndex++] = nums[r++];
            } else if (nums[l] <= nums[r]) {
                tmp[tmpIndex++] = nums[l++];
            }
        }
        while (l <= middle) {
            tmp[tmpIndex++] = nums[l++];
        }
        while (r <= end) {
            tmp[tmpIndex++] = nums[r++];
        }
        tmpIndex = 0;
        while (start<=end) {
            nums[start++] = tmp[tmpIndex++];
        }
    }

}
