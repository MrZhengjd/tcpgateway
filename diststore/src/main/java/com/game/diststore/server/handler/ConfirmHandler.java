package com.game.diststore.server.handler;


import com.game.mj.cache.FastChannelInfo;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.eventdispatch.EventAnnotationManager;
import com.game.mj.model.*;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.mj.util.JWTUtil;
import com.game.network.cache.ChannelMap;
import io.jsonwebtoken.ExpiredJwtException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Log4j2
public class ConfirmHandler extends SimpleChannelInboundHandler<GameMessage> {
    private static Logger logger = LoggerFactory.getLogger(ConfirmHandler.class);
//    private static Map<String,Integer> connectMap = new HashMap<>();
    private ChannelMap channelMap;

    private DynamicRegisterGameService dynamicRegisterGameService;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    private EventAnnotationManager eventAnnotationManager;
//    private JsonRedisManager jsonRedisManager;

    private static final long delay = 15000;
    private volatile boolean confirmSuccess = false;// 标记连接是否认证成功
    private ScheduledFuture<?> future;// 定时器的返回值
//    private static Logger
    private TokenBody tokenBody;
//    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();

    public TokenBody getTokenBody() {
        return tokenBody;
    }

    public ConfirmHandler(ChannelMap channelMap) {
        this.channelMap = channelMap;

//        this.jsonRedisManager = jsonRedisManager;
    }
    public ConfirmHandler(ChannelMap channelMap,DynamicRegisterGameService dynamicRegisterGameService) {
        this.channelMap = channelMap;
        this.dynamicRegisterGameService = dynamicRegisterGameService;

//        this.jsonRedisManager = jsonRedisManager;
    }
    public ConfirmHandler(ChannelMap channelMap, EventAnnotationManager eventAnnotationManager) {
       this(channelMap);
       this.eventAnnotationManager = eventAnnotationManager;
//        this.jsonRedisManager = jsonRedisManager;
    }
    public ConfirmHandler(ChannelMap channelMap, EventAnnotationManager eventAnnotationManager,DynamicRegisterGameService dynamicRegisterGameService) {
        this(channelMap,eventAnnotationManager);

        this.dynamicRegisterGameService = dynamicRegisterGameService;
//        this.jsonRedisManager = jsonRedisManager;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
//

        future = ctx.channel().eventLoop().schedule(()->{

            if (!confirmSuccess){
                ctx.close();
            }else {

            }
        },delay, TimeUnit.MILLISECONDS);

        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        if (future != null){
            future.cancel(true);
        }
        if (tokenBody != null){
            long playerId = tokenBody.getPlayerId();
            this.channelMap.removeChannle(playerId);
//            channelService.removeChannel(playerId,ctx.channel());
        }

        ctx.fireChannelInactive();
    }




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage message) throws Exception {
//        Message message = (Message) msg;
        if (message.getHeader().getServiceId() == RequestMessageType.AUTH_REQUEST){
            logger.info("here is the handle for auth message "+message.getHeader());
//            Body body = (Body) message.getBody();
//            String token = body.getMessage().toString();
//            String token = dataSerialize.deserialize(message.getData(),String.class);
//            String token = dataSerialize.deserialize(message.getData(),String.class);
            String token = (String) message.deserialzeToData(String.class);
            if (StringUtils.isEmpty(token)){
                logger.info("token is empty close it----");
                ctx.close();
                return;
            }
            try {

                tokenBody = JWTUtil.getTokenBody(token);
                if (checkPlayerInGame(tokenBody.getPlayerId())) {
                    sendOriginMessage(ctx);
                    ctx.close();
                    return;
                }
                repeatConnect();
                this.confirmSuccess = true;
                this.channelMap.addChannel(tokenBody.getPlayerId(),ctx.channel());
                //保存玩家登录服务器信息 后面用来别的服务转发消息
//                playerServerRepository.savePlayerServer(Constants.PLAYER_SERVER,tokenBody.getPlayerId(),Constants.SERVER_ID);
//                jsonRedisManager.setObjectHash1(Constants.PLAYER_SERVER,String.valueOf(tokenBody.getPlayerId()),Constants.SERVER_ID);
//                System.out.println("use time "+(System.currentTimeMillis() - timeMillis));
//                channelService.addChannel(tokenBody.getPlayerId(),ctx.channel());
//                GameMessage success = new DefaultGameMessage();
//                success.setHeader(new THeader(MessageType.PUSH.value));
//                success.setMessageData("success");
////                Message success = Message.buildAuthResponse(2000,"sucees");
                 GameMessage response = dynamicRegisterGameService.getResponseInstanceByMessageId(RequestMessageType.AUTH_REQUEST);
                 response.setMessageData(ResponseVo.success(FastChannelInfo.getChannelInfo()));

                 ChannelFuture future = ctx.channel().writeAndFlush(response);
                 future.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()){
                            logger.info("here is send error"+FastChannelInfo.getChannelInfo());
                        }else {
                            logger.info("error "+future.cause());
                        }
                    }
                 });

            }catch (Exception e){
                logger.info("here is error ------------");
                e.printStackTrace();
                if (e instanceof ExpiredJwtException){

                    ctx.close();
                }else {
                    ctx.close();
                }
            }

        }else {
            if (!confirmSuccess){
                ctx.close();
                logger.info("here is channel close "+(tokenBody == null ? " null ":tokenBody.getPlayerId()));
                return;
            }
            ctx.fireChannelRead(message);
        }
    }

    //发送原来游戏的服务器信息
    private void sendOriginMessage(ChannelHandlerContext ctx) {

    }

    //如果玩家正在其他游戏就不让他再这里登陆，返回原登陆的服务器地址
    private boolean checkPlayerInGame(long playerId) {
        return false;
    }


    //里面的方法不对，应该通过redis来获取用户所在的服务器，
    //如果所在的服务器存在，就给那个服务器发送下线的消息，同时通知用户异地登陆
    private void repeatConnect() {
        if (tokenBody != null){
            PlayerChannel playerChannel = channelMap.getByPlayerId(tokenBody.getPlayerId());
            if (playerChannel  == null){
                return;
            }
            Channel exist = playerChannel.getChannel();
            if (exist != null){
                exist.close();

            }
        }
    }

}
