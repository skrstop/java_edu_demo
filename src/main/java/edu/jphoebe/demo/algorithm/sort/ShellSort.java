package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-05 17:50:34
 */
public class ShellSort {

    public static void main(String[] args) {

        int size = 10;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }
        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        for (int i = array.length / 2; i > 0; i /= 2) {
            for (int j = i; j < array.length; j++) {
                int k;
                int tmp = array[j];
                // move
                for (k = j; (k - i) >= 0 && tmp < array[k - i]; k -= i) {
                    array[k] = array[k - i];
                }
                if (k != j) {
                    array[k] = tmp;
                }
                // swap
//                for (k = j; (k - i) >= 0 && tmp < array[k - i]; k -= i) {
//                    int itemTmp = array[k];
//                    array[k] = array[k - i];
//                    array[k - i] = itemTmp;
//                }
            }
        }

        System.out.println("排序后：" + JSONUtil.toJsonStr(array));


    }


}
