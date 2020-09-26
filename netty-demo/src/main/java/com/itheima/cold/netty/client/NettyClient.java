package com.itheima.cold.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 绑定端口，发送数据
 */
public class NettyClient {
    private String ip;
    private int port;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 客户端的连接程序
     */
    public void connect(){
        //1.定义线程池（发送数据的线程池）
        EventLoopGroup selector = new NioEventLoopGroup();
        //2.定义启动引导类
        Bootstrap bootstrap = new Bootstrap();
        //3.设置启动器的属性
        bootstrap.group(selector)    //配置线程池
            .channel(NioSocketChannel.class)    //设置管道Channel
            .option(ChannelOption.SO_KEEPALIVE,true)   //channel保持长连接状态
            .handler(new ChannelInitializer<SocketChannel>() {   //绑定发送事件处理器
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //4.注册处理程序
                    socketChannel.pipeline().addLast(new ClientHandler());
                }
            });

        //5.连接服务器
        ChannelFuture channelFuture = bootstrap.connect(ip, port);
        channelFuture.channel().closeFuture();

    }

    public static void main(String[] args) {
        //启动客户端
        new NettyClient("127.0.0.1",10010).connect();
    }
}
