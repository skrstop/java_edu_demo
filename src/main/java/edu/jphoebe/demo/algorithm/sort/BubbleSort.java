package edu.jphoebe.demo.algorithm.sort;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author 蒋时华
 * @date 2022-08-03 11:01:45
 */
public class BubbleSort {

    public static void main(String[] args) {


        int size = 20;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = RandomUtil.randomInt(0, 1000);
        }

        System.out.println("排序前：" + JSONUtil.toJsonStr(array));

        for (int i = 0; i < size; i++) {
            for (int i1 = size - 1; i1 > i; i1--) {
                if (array[i1] >= array[i1 - 1]) {
                    continue;
                }
                int tmp = array[i1];
                array[i1] = array[i1 - 1];
                array[i1 - 1] = tmp;
            }
        }


        System.out.println("排序后：" + JSONUtil.toJsonStr(array));
    }

}
