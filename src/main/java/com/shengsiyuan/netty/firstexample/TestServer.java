package com.shengsiyuan.netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.awt.*;

public class TestServer {
    public static  void main(String[] args) {
        //定义两个事件循环组,就是两个死循环，一直进行监听
        EventLoopGroup bossGroup = new NioEventLoopGroup();//只获取接收连接，不做后续处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();//处理数据

        try {
            //做启动封装
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)//一个channel只能注册在一个EventLoop上
                    .childHandler(new TestServerInitializer());//请求自定义处理器,childHandler 表示有workergroup来处理，handler表示由Bosshandler来处理

            //设置服务端口
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
