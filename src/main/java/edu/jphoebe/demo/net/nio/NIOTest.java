package edu.jphoebe.demo.net.nio;

import java.util.Random;

/**
 * Test class
 *
 * @author 蒋时华
 * @date 2019/8/28
 */
public class NIOTest {

    public static void main(String[] args) throws Exception {
        //运行服务器
        Server.start();
        //避免客户端先于服务器启动前执行代码
        Thread.sleep(1000);
        //运行客户端
        Client.start();
        Thread.sleep(3000);

        Random random = new Random(System.currentTimeMillis());
        char operators[] = {'+', '-', '*', '/'};
//        String expression = random.nextInt(10)+""+operators[random.nextInt(4)]+(random.nextInt(10)+1);
//        while(Client.sendMsg(new Scanner(System.in).nextLine()));
        while (
                Client.sendMsg(random.nextInt(10) + "" + operators[random.nextInt(4)] + (random.nextInt(10) + 1))) {
            Thread.sleep(random.nextInt(1000));
        }
        ;
    }

}
