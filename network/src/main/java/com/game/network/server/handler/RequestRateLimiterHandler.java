package com.game.network.server.handler;


import com.game.mj.model.GameMessage;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestRateLimiterHandler extends SimpleChannelInboundHandler<GameMessage> {
    private static Logger logger = LoggerFactory.getLogger(RequestRateLimiterHandler.class);
    private RateLimiter rateLimiter;//限流器
    private static RateLimiter userRateLimiter;//用户限流器
    private long lastClientSeqId = 0;

    public RequestRateLimiterHandler(RateLimiter rateLimiter, double requestPerSecond) {
        this.rateLimiter = rateLimiter;
        userRateLimiter = RateLimiter.create(requestPerSecond);
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage message) throws Exception {
        if (!userRateLimiter.tryAcquire()){
            logger.info("so many user request");
            ctx.close();
            return;
        }
        if (!rateLimiter.tryAcquire()){
            logger.info("so many request");
            ctx.close();
            return;
        }
//        if (lastClientSeqId > 0 && message.getHeader().getSeqId() < lastClientSeqId){
//            return;
//        }
//        this.lastClientSeqId = message.getHeader().getSeqId();

        ctx.fireChannelRead(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
