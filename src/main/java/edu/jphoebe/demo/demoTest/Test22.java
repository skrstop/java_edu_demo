package edu.jphoebe.demo.demoTest;

import java.util.Scanner;

/**
 * @author 蒋时华
 * @date 2022-09-08 12:05:37
 */
public class Test22 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String inputStr = null;
        if (scanner.hasNextLine()) {
            inputStr = scanner.nextLine();
        }
        System.out.println("Hello " + inputStr);

    }

}
