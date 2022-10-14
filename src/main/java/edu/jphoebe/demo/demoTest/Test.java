package edu.jphoebe.demo.demoTest;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author 蒋时华
 * @date 2022-08-15 09:26:49
 */
public class Test {


    public static void main(String[] args) {

        int[] height = new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        int sum = 0;
        int start = height[0];
        Deque<Integer> tmp = new ArrayDeque<>();
        for (int num : height) {

            if (tmp.peek() == null || num < tmp.peek()) {
                tmp.push(num);
                continue;
            }
            // 计算积水
            while (tmp.peek() != null) {
                Integer poll = tmp.poll();
                sum += (start - poll);
            }
            tmp.push(num);
            start = num;
        }
        System.out.println("aaa");

        // 1. 两数之和
        // 5. 最长回文子串
        // 20. 有效的括号
        // 22. 括号生成
        // 25. K 个一组翻转链表


        // 35. 搜索插入位置
        // 36. 有效的数独
        // 42. 接雨水
        // 50. Pow(x, n)
        // 91. 解码方法
        // 102. 二叉树的层序遍历
        // 121. 买卖股票的最佳时机
        // 133. 克隆图
        // 146. LRU 缓存
        // 198. 打家劫舍
        // 200. 岛屿数量
        // 295. 数据流的中位数
        // 301. 删除无效的括号
        // 322. 零钱兑换
        // 428. 序列化和反序列化 N 叉树
        // 487. 最大连续1的个数 II
        // 509. 斐波那契数
        // 516. 最长回文子序列
        // 529. 扫雷游戏
        // 674. 最长连续递增序列
        // 739. 每日温度
        // 796. 旋转字符串
        // 868. 二进制间距
        // 1048. 最长字符串链
        // 1122. 数组的相对排序
        // 1141. 查询近30天活跃用户数
        // 1142. 过去30天的用户活动 II
        // 1143. 最长公共子序列
        // 1170. 比较字符串最小字母出现频次
        // 1366. 通过投票对团队排名
        // 1487. 保证文件名唯一
        // 剑指 Offer 40. 最小的k个数
        // 剑指 Offer 61. 扑克牌中的顺子
        // 面试题 08.06. 汉诺塔问题

    }

}
