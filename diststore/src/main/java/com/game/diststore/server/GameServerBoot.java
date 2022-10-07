package com.game.diststore.server;

import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.network.cache.ChannelMap;
import com.game.network.server.NettyServer;
import com.game.network.server.SuccessHandle;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.ChannelInitializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GameServerBoot {

    @Resource
    private ChannelMap channelMap;

    @Resource
    private DynamicRegisterGameService dynamicRegisterGameService;

    private RateLimiter globalRateLimiter;
    @Resource
    private NettyServer nettyServer;

    public void startServer(String host, int port, ChannelInitializer initializer) {
        globalRateLimiter = RateLimiter.create(3000);

        nettyServer.setServerBoot(nettyServer.serverBootstrap(initializer));
        nettyServer.start(host, port);
    }

    public void startServer(String host, int port, ChannelInitializer initializer, SuccessHandle successHandle) {
        globalRateLimiter = RateLimiter.create(3000);

        nettyServer.setServerBoot(nettyServer.serverBootstrap(initializer));
        nettyServer.start(host, port,successHandle);
    }


}
