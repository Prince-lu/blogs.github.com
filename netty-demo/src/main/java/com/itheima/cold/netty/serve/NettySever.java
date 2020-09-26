package com.itheima.cold.netty.serve;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 开启端口，接收数据
 */
public class NettySever {

    private int port;

    public NettySever(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        //1.定义线程组
        //接收请求的线程组
        EventLoopGroup boos = new NioEventLoopGroup();
        //具体分发请求的线程组
        EventLoopGroup works = new NioEventLoopGroup();

        //2.定义启动引导类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //3.设置启动器的属性、绑定事件处理器handler
        bootstrap.group(boos,works)    //配置线程组
            .channel(NioServerSocketChannel.class)    //配置管道channel
            .childHandler(new ChannelInitializer<SocketChannel>() {   //绑定请求事件处理器
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //注册请求事件具体处理程序
                    socketChannel.pipeline().addLast(new ServerHandler());
                }
            })
                .option(ChannelOption.SO_BACKLOG,128)       //channel对接的Queue队列的长度
                .childOption(ChannelOption.SO_KEEPALIVE,true);   //channel保持长连接状态

        //4.绑定端口
        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            //关闭管道之后进行同步
            channelFuture.channel().closeFuture().sync();
        }finally {
            //关闭接受请求的线程组
            boos.shutdownGracefully();
            //关闭分发请求的线程组
            works.shutdownGracefully();
        }

    }


    public static void main(String[] args) {
        try {
            //5.启动服务
            new NettySever(10010).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
