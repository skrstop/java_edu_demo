package edu.jphoebe.demo.demoTest;

import java.util.Scanner;

/**
 * @author 蒋时华
 * @date 2022-09-07 17:14:02
 */
public class BiShi3 {

    public static void main(String[] args) {

        int count = 0;
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            count = Integer.parseInt(scanner.nextLine());
        }

        long count1 = 1;
        long count2 = 2;
        long count3 = 4;
        if (count == 1) {
            System.out.println(count1);
            return;
        }
        if (count == 2) {
            System.out.println(count2);
            return;
        }
        if (count == 3) {
            System.out.println(count3);
            return;
        }
        long[] dp = new long[count + 1];
        dp[0] = 0;
        dp[1] = count1;
        dp[2] = count2;
        dp[3] = count3;
        for (int i = 4; i < count + 1; i++) {
            dp[i] = (dp[i - 1] + dp[i - 2] + dp[i - 3]);
        }
        System.out.println(dp[count]);
    }

}
