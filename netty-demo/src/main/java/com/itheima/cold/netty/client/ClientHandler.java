package com.itheima.cold.netty.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuffer> {

    //往channel里面发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //往缓冲区里面写入数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("测试数据", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuffer byteBuffer) throws Exception {

    }
}
