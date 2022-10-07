package com.game.diststore.server.handler;

import com.game.mj.concurrent.LocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.GameMessage;
import com.game.mj.util.MessageKeyUtil;
import com.game.mj.messagedispatch.GameMessageDispatchService;
import com.game.network.cache.ChannelMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author zheng
 */
//@ChannelHandler.Sharable
@Log4j2
public class TGameDispatchHandler extends SimpleChannelInboundHandler<GameMessage> {
    //    private DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();
//    private JsonRedisManager jsonRedisManager;
    private DynamicRegisterGameService dynamicRegisterGameService;
    private GameMessageDispatchService dispatchService;
    public static AtomicInteger nConnection = new AtomicInteger();
    private ChannelMap channelMap;
    public static AtomicLong handleCount = new AtomicLong(0);

    //    private static GameStatusHandleMap gameStatusHandleMap
    public TGameDispatchHandler(ChannelMap channelMap) {

        this.channelMap = channelMap;
    }


    public TGameDispatchHandler(ChannelMap channelMap, DynamicRegisterGameService dynamicRegisterGameService, GameMessageDispatchService dispatchService) {

        this.channelMap = channelMap;
        this.dispatchService = dispatchService;
        this.dynamicRegisterGameService = dynamicRegisterGameService;
    }


    private void handleAndSendMessage(GameMessage gameMessage, ChannelHandlerContext ctx, Map<String, Object> map) {
        handleAndSendMessage(gameMessage, ctx, map);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage message) throws Exception {

        PromiseUtil.safeExecuteWithKey(MessageKeyUtil.getMessageKey(message), new LocalRunner() {
            @Override
            public void task(Promise promise, Object object) {
                try {
                    dispatchService.sendGameMessage(message);
                    promise.setSuccess(true);

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    log.info("handle message for id "+message.getHeader().getServiceId());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    log.info("handle message for id "+message.getHeader().getServiceId());
                }
            }
        }, message).addListener(new GenericFutureListener<Future<GameMessage>>() {
            @Override
            public void operationComplete(Future<GameMessage> future) throws Exception {
                ctx.fireChannelRead(message);
            }
        });


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx,cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            nConnection.decrementAndGet();
            ctx.close();
        }
//        log.info("here is channel close "+ctx.channel().id().asShortText()+" and playerId "+(tokenBody == null ? " null ":tokenBody.getPlayerId()));
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        nConnection.getAndIncrement();
//        FastChannelInfo.setTimeUtilFastThreadLocal(ctx.channel().id().asShortText());
//        channelMap.addChannel(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        nConnection.getAndDecrement();
//        channelMap.removeChannel(ctx.channel().id().asLongText());
//        FastChannelInfo.setTimeUtilFastThreadLocal(null);

    }
}
