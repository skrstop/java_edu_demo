package edu.jphoebe.demo.demoTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2022-09-07 17:14:02
 */
public class BiShi2 {

    public static void main(String[] args) {

        int count = 0;
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            count = Integer.parseInt(scanner.nextLine());
        }
        ArrayList<Range> input = new ArrayList<>();
        while (count > 0) {
            if (scanner.hasNextLine()) {
                String[] s = scanner.nextLine().split(" ");
                input.add(new Range(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
                count--;
            }
        }
        Collections.sort(input, (o1, o2) -> {
            int a = o1.left - o2.left;
            return a == 0 ? o1.right - o2.right : a;
        });
        ArrayList<Range> result = new ArrayList<>();
        for (Range range : input) {
            if (result.size() == 0 || range.left > result.get(result.size() - 1).right) {
                //如果链表为空 或者当前区间与上一区间不重合，直接添加(当前的起始值大于下一个的终止值)
                result.add(new Range(range.left, range.right));
            } else {//否则 将重复的区间进行合并
                result.get(result.size() - 1).right = Math.max(range.right, result.get(result.size() - 1).right);
            }
        }

        System.out.println(result.stream().map(item -> "[" + item.left + ", " + item.right + "]").collect(Collectors.joining(" ")));

    }

    static class Range {
        int left;
        int right;

        public Range(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }

}
