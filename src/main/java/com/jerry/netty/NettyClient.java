package com.jerry.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Jerry.Wu
 * @description:
 * @date 2019/2/28 11:43
 */
public class NettyClient {

    public static final int MAX_RETRY = 10;

    public static void main(String args[]) {
        /**
         * 对于客户端的启动来说，和服务端的启动类似，依然需要线程模型、IO 模型，以及 IO 业务处理逻辑三大参数
         */

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                    }
                });
        bootstrap.connect("127.0.0.1", 80).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接成功！");
                } else {
                    System.out.println("连接失败！");
                }
            }
        });
    }

    /**
     * 失败重连
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败，开始重连");
                connect(bootstrap, host, port);
            }
        });
    }

    /**
     * 通常情况下，连接建立失败不会立即重新连接，而是会通过一个指数退避的方式，
     * 比如每隔 1 秒、2 秒、4 秒、8 秒，以 2 的幂次来建立连接，然后到达一定次数之后就放弃连接，
     * 接下来我们就来实现一下这段逻辑，我们默认重试 5 次
     * @param bootstrap
     * @param host
     * @param port
     * @param retry
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }

}
