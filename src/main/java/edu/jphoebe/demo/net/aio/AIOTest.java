package edu.jphoebe.demo.net.aio;

import java.util.Random;

/**
 * Test class
 *
 * @author 蒋时华
 * @date 2019/8/29
 */
public class AIOTest {

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

        while (Client.sendMsg(random.nextInt(10) + "" + operators[random.nextInt(4)] + (random.nextInt(10) + 1))) {
            Thread.sleep(random.nextInt(1000));
        }
    }

}
