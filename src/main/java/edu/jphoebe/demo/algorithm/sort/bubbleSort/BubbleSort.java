package edu.jphoebe.demo.algorithm.sort.bubbleSort;

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
            for (int i1 = i + 1; i1 < size; i1++) {
                if (array[i] <= array[i1]) {
                    continue;
                }
                int temp = array[i];
                array[i] = array[i1];
                array[i1] = temp;
            }
        }


        System.out.println("排序后：" + JSONUtil.toJsonStr(array));
    }

}
