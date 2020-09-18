package edu.jphoebe.demo.netty.demo.client;

import io.netty.channel.epoll.Epoll;

/**
 * @author 蒋时华
 * @date 2020-07-22 15:57:31
 */
public class SimpleClient {

    public static void main(String[] args) {

        System.out.println(Epoll.isAvailable());

    }

}
