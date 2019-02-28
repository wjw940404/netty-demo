package com.jerry.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @author Jerry.Wu
 * @description:
 * @date 2019/2/28 11:14
 */
public class NettyServer {

    public static void main(String args[]) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup workers = new NioEventLoopGroup();
        serverBootstrap
                .group(boos, workers)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 这里可以写服务端启动时的一些逻辑
                        System.out.println("服务端启动中");
                    }
                });
        bind(serverBootstrap, 1000);
    }

    /**
     * 自动绑定递增端口
     * 从X端口往上找一个端口，直到这个端口能够绑定成功
     * @param serverBootstrap
     * @param port
     */
    private static void bind(ServerBootstrap serverBootstrap, final int port) {
        /**
         * serverBootstrap.bind();这个方法是一个异步的方法，调用之后是立即返回的，他的返回值是一个ChannelFuture，
         * 我们可以给这个ChannelFuture添加一个监听器GenericFutureListener，然后我们在GenericFutureListener的operationComplete方法里面，我们可以监听端口是否绑定成功
         */
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口绑定成功：" + port);
                } else {
                    System.out.println("端口绑定失败：" + port);
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }

}
