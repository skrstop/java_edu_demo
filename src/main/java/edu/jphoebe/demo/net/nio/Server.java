package edu.jphoebe.demo.net.nio;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * nio 服务端
 * <p>
 *     打开ServerSocketChannel，监听客户端连接
 *     绑定监听端口，设置连接为非阻塞模式
 *     创建Reactor线程，创建多路复用器并启动线程
 *     将ServerSocketChannel注册到Reactor线程中的Selector上，监听ACCEPT事件
 *     Selector轮询准备就绪的key
 *     Selector监听到新的客户端接入，处理新的接入请求，完成TCP三次握手，简历物理链路
 *     设置客户端链路为非阻塞模式
 *     将新接入的客户端连接注册到Reactor线程的Selector上，监听读操作，读取客户端发送的网络消息
 *     异步读取客户端消息到缓冲区
 *     对Buffer编解码，处理半包消息，将解码成功的消息封装成Task
 *     将应答消息编码为Buffer，调用SocketChannel的write将消息异步发送给客户端
 * <p>
 * <p>
 * 多路复用器|选择某个通道器(Selector)
 * 选择器类管理着一个被注册的通道集合的信息和它们的就绪状态。
 * 通道是和选择器一起被注册的，并且使用选择器来更新通道的就绪状态，
 * 当这么做的时候，可以选择将被激发的线程挂起直到有就绪的通道。
 * 使用Selector的好处在于： 使用更少的线程来就可以来处理通道了， 相比使用多个线程，避免了线程上下文切换带来的开销。
 * <p>
 * <p>
 * SelectionKey
 * 表示了一个特定的通道对象和一个特定的选择器对象之间的注册关系。
 * key.attachment(); //返回SelectionKey的attachment，attachment可以在注册channel的时候指定。
 * key.channel(); // 返回该SelectionKey对应的channel。
 * key.selector(); // 返回该SelectionKey对应的Selector。
 * key.interestOps(); //返回代表需要Selector监控的IO操作的bit mask
 * key.readyOps(); // 返回一个bit mask，代表在相应channel上可以进行的IO操作。
 *
 * @author 蒋时华
 * @date 2019/8/28
 */
public class Server {
    private static int DEFAULT_PORT = 12345;
    private static ServerHandle serverHandle;

    public static void start() {
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port) {
        if (serverHandle != null) {
            serverHandle.stop();
        }
        serverHandle = new ServerHandle(port);
        new Thread(serverHandle, "Server").start();
    }
}

class ServerHandle implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean started;

    /**
     * 构造方法
     *
     * @param port 指定要监听的端口号
     */
    public ServerHandle(int port) {
        try {
            //创建选择器
            selector = Selector.open();
            //打开监听通道
            serverChannel = ServerSocketChannel.open();
            //如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
            serverChannel.configureBlocking(false);//开启非阻塞模式
            //绑定端口 backlog设为1024
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            //监听客户端连接请求
            // 如果你对不止一种事件感兴趣，使用或运算符即可，如下：
            // int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            //标记服务器已开启
            started = true;
            System.out.println("服务器已启动，端口号：" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        //循环遍历selector
        while (started) {
            try {
                // 阻塞,阻塞到至少有一个通道在你注册的事件上就绪了。
                // 这个过程可能会造成调用线程进入阻塞状态, 通过调用Selector对象的wakeup（）方法让处在阻塞状态的select()方法立刻返回
                // wakeup（）该方法使得选择器上的第一个还没有返回的选择操作立即返回。如果当前没有进行中的选择操作，那么下一次对select()方法的一次调用将立即返回。
//				selector.select();
                // 和select()一样，但最长阻塞时间为timeout毫秒。
//                selector.select(1000);
                // 非阻塞，只要有通道就绪就立刻返回。
                selector.selectNow();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        //selector关闭后会自动释放里面管理的资源
        if (selector != null) {
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理新接入的请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //通过ServerSocketChannel的accept创建SocketChannel实例
                //完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                SocketChannel sc = ssc.accept();
                //设置为非阻塞的
                // 神奇的事情，blocking默认是true
                // TODO: 2019/8/28
                sc.configureBlocking(false);
                //注册为读
                sc.register(selector, SelectionKey.OP_READ);
            }
            //读消息
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                //创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //读取请求码流，返回读取到的字节数
                int readBytes = sc.read(buffer);
                //读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    //将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
                    buffer.flip();
                    //根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    //将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);
                    String expression = new String(bytes, "UTF-8");
                    System.out.println("服务器收到消息：" + expression);
                    //处理数据
                    String result = null;
                    try {
                        result = Calculator.cal(expression).toString();
                    } catch (Exception e) {
                        result = "计算错误：" + e.getMessage();
                    }
                    //发送应答消息
                    doWrite(sc, result);
                }
                //没有读取到字节 忽略
//				else if(readBytes==0);
                //链路已经关闭，释放资源
                else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    //异步发送应答消息
    private void doWrite(SocketChannel channel, String response) throws IOException {
        //将消息编码为字节数组
        byte[] bytes = response.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        //flip操作
        writeBuffer.flip();
        //发送缓冲区的字节数组
        channel.write(writeBuffer);
        //****此处不含处理“写半包”的代码
    }
}

final class Calculator {
    private final static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    public static Object cal(String expression) throws ScriptException {
        return jse.eval(expression);
    }
}
