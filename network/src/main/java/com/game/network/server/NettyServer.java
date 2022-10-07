package com.game.network.server;



import com.game.mj.constant.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

@Getter
@Setter
@Component
public class NettyServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture f;
    private ChannelInitializer initializer ;
    private ServerBootstrap b;
    private int serverId = Constants.SERVER_ID;
    public volatile boolean isMaster = false;
    public NettyServer() {
//        b = serverBootstrap();
    }
    public NettyServer(ChannelInitializer initializer) {
        this.initializer = initializer;
        b = serverBootstrap();
    }

    public void start(String host,int port){
//        ServerBootstrap b = serverBootstrap();
        try {
            if (StringUtils.isNotBlank(host)){
                f = b.bind(host,port).sync();
            }else {
                f = b.bind(port).sync();
            }
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public void multiStart(String host,int beginPort,int count){
//        ServerBootstrap b = serverBootstrap();
        try {

            for (int i = 0; i < count; i++) {
                int port = beginPort + i;
                ChannelFuture sync = this.b.bind(port).addListener((ChannelFutureListener) future -> {
                    System.out.println("bind success in port: " + port);
                }).sync();
                sync.channel().closeFuture();

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setServerBoot(ServerBootstrap b) {
        this.b = b;
    }

    public void start(String host, int port, SuccessHandle successHandle){
//        ServerBootstrap b = serverBootstrap();
        try {
            if (StringUtils.isNotBlank(host)){
                f = b.bind(new InetSocketAddress(host,port)).sync();
            }else {
                f = b.bind(port).sync();
            }
//
            f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("here is bin sucees");
                        successHandle.afterSueccess();
                    }
                }
            });
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ServerBootstrap serverBootstrap(){
       return serverBootstrap(this.initializer);
    }
    public ServerBootstrap serverBootstrap(ChannelInitializer initializer){
        ThreadFactory threadRpcFactory = new NamedThreadFactory("Netty ThreadFactory");
        bossGroup = new NioEventLoopGroup(3);
        workerGroup = new NioEventLoopGroup(Constants.PARALLEL, threadRpcFactory, SelectorProvider.provider());
        ServerBootstrap bootstrap = new ServerBootstrap();
//        bootstrap.;
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024*2)
                .childOption(ChannelOption.SO_TIMEOUT,6000)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)

//                .option(ChannelOption.TCP_NODELAY,true)
//                .option(ChannelOption.SO_TIMEOUT,6000)

                .childOption(ChannelOption.SO_RCVBUF,Integer.MAX_VALUE)

//                .childOption(ChannelOption.SO_REUSEADDR,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childHandler(initializer);
        return bootstrap;
    }


}
