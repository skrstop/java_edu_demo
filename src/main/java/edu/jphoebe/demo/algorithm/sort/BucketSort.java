package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2022-08-03 11:01:45
 */
public class BucketSort {

    public static void main(String[] args) {


        int size = 20;
//        Integer[] array = new Integer[size];
//        for (int i = 0; i < size; i++) {
//            array[i] = RandomUtil.randomInt(0, 1000);
//        }
        Integer[] array = {63, 486, 306, 649, 773, 680, 279, 431, 353, 827, 788, 130, 46, 304, 607, 983, 34, 815, 39, 637};

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        // 获取最大值和最小值
        int min = array[0], max = array[0];
        for (int i = 1; i < size; i++) {
            if (array[i] > max) {
                max = array[i];
            } else if (array[i] < min) {
                min = array[i];
            }
        }
        // 桶个数
        int bucketNum = (max - min) / size + 1;
        // 初始化桶
        List<List<Integer>> bucketValue = new ArrayList<>();
        for (int i = 0; i < bucketNum; i++) {
            bucketValue.add(new ArrayList<>());
        }
        // 将数据放在桶中
        for (int i = 0; i < size; i++) {
            // 定位桶的位置
            int bucketIndex = (array[i] - min) / size;
            bucketValue.get(bucketIndex).add(array[i]);
        }
        // 对每个桶的数据进行排序
        for (List<Integer> values : bucketValue) {
            if (values.isEmpty()) {
                continue;
            }
            // 使用归并排序，保证稳定性
            mergeSort(values, 0, values.size() - 1);
        }
        // 将数据回写到数组中
        int index = 0;
        for (List<Integer> values : bucketValue) {
            for (Integer value : values) {
                array[index] = value;
                index++;
            }
        }

        System.out.println("排序后：" + JSONUtil.toJsonStr(array));
    }

    private static void mergeSort(List<Integer> values, int start, int end) {
        if (start >= end) {
            return;
        }
        int middle = (end - start) / 2;
        mergeSort(values, start, middle);
        mergeSort(values, middle + 1, end);
        merge(values, start, end, middle);
    }

    private static void merge(List<Integer> values, int start, int end, int middle) {
        List<Integer> tmp = new ArrayList<>(end - start + 1);
        int l = start, r = middle + 1, tmpIndex = 0;
        while (l <= middle && r <= end) {
            if (values.get(l) <= values.get(r)) {
                tmp.add(values.get(l));
                l++;
                tmpIndex++;
            } else if (values.get(l) >= values.get(r)) {
                tmp.add(values.get(r));
                r++;
                tmpIndex++;
            }
        }
        while (l <= middle) {
            tmp.add(values.get(l));
            l++;
            tmpIndex++;
        }
        while (r <= end) {
            tmp.add(values.get(r));
            r++;
            tmpIndex++;
        }
        tmpIndex = 0;
        while (start <= end) {
            values.set(start, tmp.get(tmpIndex));
            start++;
            tmpIndex++;
        }
    }


}
