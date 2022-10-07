package com.game.network.client;

import com.game.mj.cache.MasterInfo;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.concurrent.IGameEventExecutorGroup;
import com.game.mj.concurrent.NonResultLocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.constant.InfoConstant;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.messagedispatch.GameMessageDispatchService;
import com.game.mj.model.ChannelInfo;
import com.game.mj.model.GameMessage;
import com.game.mj.util.JWTUtil;
import com.game.mj.util.TopicUtil;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
@Component
public class ConnectTools {
    private static Logger logger = LoggerFactory.getLogger(ConnectTools.class);
    private ClientFactory clientFactory = ClientFactory.getInstance();
    private IGameEventExecutorGroup executorService = new IGameEventExecutorGroup(4,null);
    private ReadWriteLockOperate lockOperate = new ReadWriteLockOperate();
    @Contended
    private volatile boolean connectSuccess;
    @Contended
    private volatile boolean startConnect;
    @Contended
    private volatile ChannelFuture channelFuture;
    @Autowired
    private ClientChannelMap clientChannelMap;
    private Map<String, ChannelInfo> startMap = new HashMap<>();
    @Autowired
    private GameMessageDispatchService gameMessageDispatchService;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    private EventExecutor eventExecutor = new DefaultEventExecutor();
//    @Autowired
//    private SendMessageSerialize sendMessageSerialize;
    public ChannelInfo getByKey(String key){
        return startMap.get(key);
    }
    @PostConstruct
    public void afterInit(){
        init();
    }
    public void init() {
        clientFactory.setGameMessageDispatchService(gameMessageDispatchService);
        clientFactory.setDynamicRegisterGameService(dynamicRegisterGameService);
        clientFactory.buildNewClient();

//        clientChannelMap.saveChannelFuture(TopicUtil.generateTopic(InfoConstant.LOCAL_HOST,InfoConstant.MASTER_PORT),connect);
    }

    /**
     * 链接到recorder
     */
    public void connectRecorder() {
        connctServer(InfoConstant.LOCAL_HOST, InfoConstant.RECORDER_PORT);

    }
    public void writeToRecorder(GameMessage gameMessage) {
        writeMessage(InfoConstant.LOCAL_HOST, InfoConstant.RECORDER_PORT,gameMessage);
    }

    public void writeMessage(InetSocketAddress address,GameMessage gameMessage){
        writeMessage(address.getHostName(),address.getPort(),gameMessage);
    }
    /**
     * 给指定端口服务发送数据
     * @param host
     * @param port
     * @param gameMessage
     */
    public void writeMessage(String host, Integer port, GameMessage gameMessage) {
//        lockOperate.writeLockOperation(new Operation() {
//            @Override
//            public void operate() {
//               readyMessage(host,port,gameMessage);
//            }
//        });
//
//        SendMessageSerialize.sendMessage(this,host,port,gameMessage);
        sendMessageInNewThread(host,port,gameMessage,this);
    }

    public void sendMessageInNewThread(String host, Integer port, GameMessage gameMessage,ConnectTools tools){
        PromiseUtil.safeExecuteNonResult(executorService.selectByHash(TopicUtil.generateTopic(host,port)), new NonResultLocalRunner() {
            @Override
            public void task() {
                SendMessageSerialize.sendMessage(tools,host,port,gameMessage);
            }
        });

    }

    public void handleFutureResult(String key,NonResultLocalRunner localRunner){
        PromiseUtil.safeExecuteNonResult(executorService.selectByHash(key),localRunner);
    }
    /**
     * 没有连接的就建立连接，并发送token
     * @param host
     * @param port
     * @param gameMessage
     */
    public void readyMessage(String host,Integer port,GameMessage gameMessage){
        String key = TopicUtil.generateTopic(host, port);
        if (clientChannelMap.getByKey(key) == null && startMap.get(key) == null) {
            connctServer(host, port);
        }
        long timeMillis = System.currentTimeMillis();
        try {
            startMap.get(key).getLatch().await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("finish time "+(System.currentTimeMillis() - timeMillis));
        gameMessage.getHeader().setPlayerId(MasterInfo.getPlayerId());
        ChannelFuture channelFuture = clientChannelMap.getByKey(key);channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                channelFuture.channel().writeAndFlush(gameMessage).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()){
                            logger.info("here is send message success "+gameMessage.getHeader().getServiceId() + " to remote "+channelFuture.channel().remoteAddress().toString());
                        }
                    }
                });
            }
        });

    }

    /**
     * 只是建立链接
     * @param host
     * @param port
     */
    public ChannelFuture onlyConnectServer(String host, Integer port,Map<String, ChannelInfo> channelFutureMap){
        try {
            ChannelFuture sync = clientFactory.connect(host, port).sync();
            if (sync != null){
                ChannelInfo channelInfo = new ChannelInfo();
                channelInfo.setHost(host);
                channelInfo.setPort(port);
                channelInfo.setChannelFuture(sync);
                channelInfo.setStatus(1);
                channelFutureMap.put(TopicUtil.generateTopic(host,port),channelInfo);
            }else {
                logger.info("channel future is empty");
            }
            return sync;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送请求消息
     * @param channelFuture
     */
    public ChannelFuture sendAuthMessage(ChannelFuture channelFuture){

//                        fu.channel().flush();
        try {
            GameMessage auth = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.AUTH_REQUEST);
            String token = JWTUtil.getUserToken("12433", MasterInfo.getPlayerId(),MasterInfo.getPlayerId(),"1");
            auth.setMessageData(token);
            return channelFuture.channel().writeAndFlush(auth).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 连接到服务区
     *
     * @param host
     * @param port
     */
    public void connctServer(String host, Integer port) {
        try {
            ChannelFuture fu = clientFactory.connect(host, port).sync();
            String key = TopicUtil.generateTopic(host, port);
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setChannelFuture(fu);
            startMap.put(key, channelInfo);
            fu.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        clientChannelMap.saveChannelFuture(key, fu);
//                        clientChannelMap.notifyAll();

                        GameMessage auth = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.AUTH_REQUEST);
                        String token = JWTUtil.getUserToken("12433", MasterInfo.getPlayerId(),MasterInfo.getPlayerId(),"1");
                        auth.setMessageData(token);
//                        fu.channel().flush();
                        fu.channel().writeAndFlush(auth).addListener(new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                if (future.isSuccess()){
                                    logger.info("write confirm message success---------------");
                                    startMap.get(key).getLatch().countDown();

                                }
                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 发送消息
     *
     * @param gameMessage
     */
    private void sendMessage2(GameMessage gameMessage) {
        this.channelFuture.channel().writeAndFlush(gameMessage).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("send message here sucess");
                } else {
                    logger.info("cannot send the message " + future.cause());
                }
            }
        });
    }
}
