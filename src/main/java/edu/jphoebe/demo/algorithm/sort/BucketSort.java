package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;

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
        Integer[] nums = {5, 2, 3, 1};
        size = nums.length;

        System.out.println("排序前：" + JSONUtil.toJsonStr(nums));

        // 获取最大值和最小值
        int max = nums[0], min = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (max < nums[i]) {
                max = nums[i];
            }
            if (min > nums[i]) {
                min = nums[i];
            }
        }
        int bucketCount = (max - min) / nums.length + 1;
        ArrayList<ArrayList<Integer>> bucket = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            bucket.add(new ArrayList<>());
        }
        for (int i = 0; i < nums.length; i++) {
            int index = (nums[i] - min) / nums.length;
            bucket.get(index).add(nums[i]);
        }
        for (int i = 0; i < bucketCount; i++) {
            ArrayList<Integer> values = bucket.get(i);
            for (int j = values.size() / 2; j > 0; j /= 2) {
                for (int l = j; l < values.size(); l++) {
                    int tmp = values.get(l), k;
                    for (k = l; (k - j) >= 0 && values.get(k - j) > tmp; k -= j) {
                        values.set(k, values.get(k - j));
                    }
                    if (k != l) {
                        values.set(k, tmp);
                    }
                }
            }
        }
        int index = 0;
        for (ArrayList<Integer> values : bucket) {
            for (Integer value : values) {
                nums[index++] = value;
            }
        }

        System.out.println("排序后：" + JSONUtil.toJsonStr(nums));
    }


}
