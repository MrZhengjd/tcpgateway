package com.game.gateway.server.handler;

import com.game.mj.concurrent.LocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.GameMessage;
import com.game.mj.model.TokenBody;
import com.game.mj.util.MessageKeyUtil;
import com.game.mj.util.TopicUtil;
import com.game.domain.consumer.SendMessageModel;
import com.game.gateway.config.GateWayConfig;
import com.game.gateway.consume.PlayerInstanceModel;
import com.game.network.cache.ChannelMap;


import com.game.mj.util.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.*;
import lombok.extern.log4j.Log4j2;
//import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;
import java.util.concurrent.ExecutionException;
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
    private PlayerInstanceModel playerInstanceModel;
//    private PlayerInstanceService playerInstanceService;
    private GateWayConfig gateWayConfig;
    private SendMessageModel sendMessageModel;
//    private KafkaTemplate<String, byte[]> kafkaTemplate;
    //jdk1.5 并发包中的用于计数的类
    public static AtomicInteger nConnection = new AtomicInteger();
    private ChannelMap channelMap;
    public static AtomicLong handleCount = new AtomicLong(0);
//    private static GameStatusHandleMap gameStatusHandleMap
    public TGameDispatchHandler(ChannelMap channelMap) {

        this.channelMap = channelMap;
    }



    public TGameDispatchHandler(ChannelMap channelMap, DynamicRegisterGameService dynamicRegisterGameService) {

        this.channelMap = channelMap;

        this.dynamicRegisterGameService = dynamicRegisterGameService;
    }
    public TGameDispatchHandler(ChannelMap channelMap, DynamicRegisterGameService dynamicRegisterGameService, PlayerInstanceModel playerInstanceModel, SendMessageModel sendMessageModel, GateWayConfig gateWayConfig) {
        this.channelMap = channelMap;

        this.dynamicRegisterGameService = dynamicRegisterGameService;
        this.playerInstanceModel = playerInstanceModel;
        this.sendMessageModel = sendMessageModel;
        this.gateWayConfig = gateWayConfig;
    }
    private TokenBody tokenBody;

    private void handleAndSendMessage( GameMessage gameMessage,ChannelHandlerContext ctx,Map<String ,Object> map){
        handleAndSendMessage(gameMessage,ctx,map);
    }
//    private void handleAndSendMessage( GameMessage gameMessage,ChannelHandlerContext ctx,Object result){
//        gameMessage.setBody(result);
//        ctx.writeAndFlush(gameMessage);
//    }

    /**
     * 获取玩家所在服务器后进行操作
     * @param ctx
     * @param moduleId
     * @param successHandler
     */
    public void operateAfterSelectServer(ChannelHandlerContext ctx, Integer moduleId,SuccessHandler successHandler,GameMessage message){

        if (tokenBody == null) {// 如果首次通信，获取验证信息
            ConfirmHandler confirmHandler = (ConfirmHandler) ctx.channel().pipeline().get("confirmHandler");
            if (confirmHandler != null){
                tokenBody = confirmHandler.getTokenBody();

            }

        }
        message.getHeader().setPlayerId(tokenBody.getPlayerId());
        Object key = MessageKeyUtil.getMessageKey(message);
        Long playerId = message.getHeader().getPlayerId();
//        ctx.fireChannelRead(message);
        PromiseUtil.safeExecuteWithKey(key, new LocalRunner<Integer>() {
            @Override
            public void task(Promise promise, Integer object) {

                Integer server = playerInstanceModel.selectServerId(playerId, moduleId);
                log.info("here is receive message "+server);
                if (server != null){
                    promise.setSuccess(server);
                }else {
                    promise.setFailure(new RuntimeException("not found it"));
                }
            }
        },null).addListener(new GenericFutureListener<Future<Integer>>() {
            @Override
            public void operationComplete(Future<Integer> future) throws Exception {
                if (future.isSuccess()){
                    successHandler.successHandler(future);
                }else {
                    System.out.println("cannot found the serverId");
                }
            }
        });

    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage message) throws Exception {
//
        operateAfterSelectServer(ctx,message.getHeader().getModuleId(), new SuccessHandler() {
            @Override
            public void successHandler(Future<Integer> future) {
                readyAndSendMessage(future,message,ctx);
                ctx.fireChannelRead(message);

            }
        },message);


    }

    /**
     * 准备并转发消息
     * @param future
     * @param message
     * @param ctx
     */
    public void readyAndSendMessage(Future<Integer> future,GameMessage message,ChannelHandlerContext ctx){
        Integer toServerId = null;
        try {
            toServerId = future.get();
            message.getHeader().setPlayerId(tokenBody.getPlayerId());
            message.getHeader().setClientIp(NettyUtils.getRemoteIP(ctx.channel()));
//
            message.getHeader().setTraceId(gateWayConfig.getServerId());
            Object key = MessageKeyUtil.getMessageKey(message);
            String topic = TopicUtil.generateTopic(playerInstanceModel.getModuleName(message.getHeader().getModuleId()), toServerId);// 动态创建与业务服务交互的消息总线Topic
//
            sendMessageModel.sendMessageToMq(message,topic,key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx,cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            nConnection.decrementAndGet();
            ctx.close();
        }
//        log.info("here is channel close "+ctx.channel().id().asShortText()+" and playerId "+(tokenBody == null ? " null ":tokenBody.getPlayerId()));
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        nConnection.getAndIncrement();


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
       nConnection.getAndDecrement();

    }
}
