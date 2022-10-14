package edu.jphoebe.demo.demoTest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2022-09-07 17:14:02
 */
public class BiShi {

    public static void main(String[] args) {

        int count = 0;
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            count = Integer.parseInt(scanner.nextLine());
        }
        String inputStr = null;
        if (scanner.hasNextLine()) {
            inputStr = scanner.nextLine();
        }
        List<Integer> collect = Arrays.stream(inputStr.split(" ")).map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> result = new ArrayList<>();
        List<Integer> tmp = new LinkedList<>();
        for (Integer target : collect) {
            if (tmp.isEmpty() || target < tmp.get(0)) {
                result.add(1);
                tmp.add(0, target);
                continue;
            } else if (target >= tmp.get(tmp.size() - 1)) {
                result.add(tmp.size() + 1);
                tmp.add(tmp.size(), target);
                continue;
            }
            int l = 0, r = tmp.size() - 1, index = 0;
            while (l <= r) {
                if (l == r) {
                    index = l + 1;
                    break;
                }
                int mid = (l + r) / 2;
                if (tmp.get(mid) > target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            result.add(index);
            tmp.add(index - 1, target);
        }
        System.out.println(result.stream().map(Object::toString).collect(Collectors.joining(" ")));


    }

}
