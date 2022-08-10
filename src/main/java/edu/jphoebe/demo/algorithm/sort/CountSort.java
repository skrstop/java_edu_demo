package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-10 10:12:35
 */
public class CountSort {

    public static void main(String[] args) {
        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        // 找最大最小值
        int min = array[0], max = array[0];
        for (int i = 0; i < size; i++) {
            if (array[i] > max) {
                max = array[i];
            } else if (array[i] < min) {
                min = array[i];
            }
        }
        // 计算数量
        int[] countTmp = new int[max - min + 1];
        for (int i = 0; i < size; i++) {
            countTmp[array[i] - min]++;
        }
        // 计算下标
        for (int i = 1; i < countTmp.length; i++) {
            countTmp[i] += countTmp[i - 1];
        }
        // 排序
        Integer[] sortArray = new Integer[size];
        for (int i = size - 1; i >= 0; i--) {
            int sort = countTmp[array[i] - min];
            countTmp[array[i] - min]--;
            sortArray[sort - 1] = array[i];
        }
        array = sortArray;

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));
    }

}
