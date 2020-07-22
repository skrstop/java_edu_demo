package edu.jphoebe.demo.net.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * * 同步和异步的概念：实际的I/O操作
 * 同步是用户线程发起I/O请求后需要等待或者轮询内核I/O操作完成后才能继续执行
 * 异步是用户线程发起I/O请求后仍需要继续执行，当内核I/O操作完成后会通知用户线程，或者调用用户线程注册的回调函数
 * <p>
 * * 阻塞和非阻塞的概念：
 * 讨论的是参与通信双方的工作机制,是否需要互相等待对方的执行
 *  阻塞：
 *      通信过程中 一方在处理通信，另一方 要等待对方执行并返回信息不能去做其他无关的事
 *  非阻塞：
 *      通信过程中 一方在处理通信，另一方 可以不用等待执行并返回信息而可以去做其他无关的事 直到对方处理通信完成 再在适合的时候继续处理通信过程
 * <p>
 * * 网络I/O的同步异步：
 * 同步和异步描述的是一种消息通知的机制，主动等待消息返回还是被动接受消息
 * 同步io指的是调用方通过主动等待获取调用返回的结果来获取消息通知，
 * 异步io指的是被调用方通过某种方式（如，回调函数）来通知调用方获取消息。
 * <p>
 * * 为什么被称为非阻塞？
 * BIO在发起读请求以后，会一直等待，知道拿到结果
 * NIO在发起读请求以后，不会立即拿到结果
 * AIO通过回调方法，被动的接受
 * <p>
 * <p>
 * BIO:同步阻塞式IO 面向流 操作字节或字符 单向传输数据
 * NIO:同步非阻塞式IO 面向通道 操作缓冲区 双向传输数据
 * NIO:同步非阻塞式IO 大量使用回调函数 异步处理通信过程 异步的双向传输数据
 */

/**
 * 同步阻塞式I/O创建的Server
 * BIO主要的问题在于每当有一个新的客户端请求接入时，服务端必须创建一个新的线程来处理这条链路，在需要满足高性能、高并发的场景是没法应用的（大量创建新的线程会严重影响服务器性能，甚至罢工）
 * 限制了线程数量，如果发生大量并发请求，超过最大数量的线程就只能等待，直到线程池中的有空闲的线程可以被复用。而对Socket的输入流就行读取时，会一直阻塞，直到发生：
 *     有数据可读
 *     可用数据以及读取完毕
 *     发生空指针或I/O异常
 * 所以在读取数据较慢时（比如数据量大、网络传输慢等），大量并发的情况下，其他接入的消息，只能一直等待，这就是最大的弊端。
 *     而NIO，就能解决这个难题
 *
 * @author 蒋时华
 * @date 2019/8/28
 */
public class ServerNormal {

    //默认的端口号
    private static int DEFAULT_PORT = 12344;
    //单例的ServerSocket
    private static ServerSocket server;

    //线程池 懒汉式的单例
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    //根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
    public static void start() throws IOException {
        //使用默认值
        start(DEFAULT_PORT);
    }

    //这个方法不会被大量并发访问，不太需要考虑效率，直接进行方法同步就行了
    public synchronized static void start(int port) throws IOException {
        if (server != null) return;
        try {
            //通过构造函数创建ServerSocket
            //如果端口合法且空闲，服务端就监听成功
            server = new ServerSocket(port);
            System.out.println("服务器已启动，端口号：" + port);
            //通过无线循环监听客户端连接
            while (true) {
                //如果没有客户端接入，将阻塞在accept操作上。
                Socket socket = server.accept();
                //当有新的客户端接入时，会执行下面的代码
                //然后创建一个新的线程处理这条Socket链路
                // TODO: 2019/8/28  问题所在
//                new Thread(new ServerHandler(socket)).start();
                executorService.execute(new ServerHandler(socket));
            }
        } finally {
            //一些必要的清理工作
            if (server != null) {
                System.out.println("服务器已关闭。");
                server.close();
                server = null;
            }
        }
    }


}
