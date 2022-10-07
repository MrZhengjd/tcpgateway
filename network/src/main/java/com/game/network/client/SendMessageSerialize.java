package com.game.network.client;

import com.game.mj.cache.MasterInfo;
import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.concurrent.NonResultLocalRunner;
import com.game.mj.model.ChannelInfo;
import com.game.mj.model.GameMessage;
import com.game.mj.util.TopicUtil;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zheng
 */
//@Component
public class SendMessageSerialize {
    private static Map<String, ChannelInfo> channelFutureMap = new HashMap<>();
    private static Map<String , LinkedBlockingQueue> messageQueue = new HashMap<>();
    private static ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public static void saveChannelInfo(String key,ChannelInfo channelInfo){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                channelFutureMap.put(key,channelInfo);
            }
        });
    }
    public static void changeInfoStatus(String key){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                ChannelInfo channelInfo = channelFutureMap.get(key);
                if (channelInfo != null){
                    channelInfo.setStatus(2);
                }
            }
        });
    }

    /**
     * 发送消息
     * @param tools
     * @param host
     * @param port
     * @param message
     */
    public static void sendMessage(ConnectTools tools, String host, Integer port, GameMessage message){
        String key = TopicUtil.generateTopic(host, port);
        message.getHeader().setPlayerId(MasterInfo.getPlayerId());
        LinkedBlockingQueue queue = messageQueue.get(key);
        if (queue == null){
            queue = new LinkedBlockingQueue();
            messageQueue.put(key,queue);
        }
        queue.add(message);
        if (!channelFutureMap.containsKey(key)){

            ChannelFuture channelFuture = tools.onlyConnectServer(host, port,channelFutureMap);
            if (channelFuture == null){
                return;
            }
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    handleFuture(future,key,tools);
                }
            });
        }else {

            ChannelInfo channelInfo = channelFutureMap.get(key);
            if (channelInfo.getStatus() == 1 && !channelInfo.isSendAuth()){
//                        System.out.println("add message to queue");
                channelInfo.setSendAuth(true);
                channelFutureMap.put(key,channelInfo);
                sendAuthMessage(tools,channelInfo,key);
            }else {
                sendMessageInSerialize(key,tools);
            }

        }


    }

    private static void handleFuture(Future<? super Void> future,String key,ConnectTools tools){
        tools.handleFutureResult(key, new NonResultLocalRunner() {
            @Override
            public void task() {
                if (future.isSuccess()){
                    System.out.println("add message to queue");
                    ChannelInfo channelInfo = channelFutureMap.get(key);
                    if (channelInfo.getStatus() == 1 && !channelInfo.isSendAuth()){
//                        System.out.println("add message to queue");
                        channelInfo.setSendAuth(true);
                        channelFutureMap.put(key,channelInfo);
                        sendAuthMessage(tools,channelInfo,key);
                    }

                }else {
                    channelFutureMap.remove(key);
                    System.out.println("fail "+future.cause());
                }
            }
        });

    }
    private static void handleAuthFuture(ConnectTools tools,ChannelInfo channelInfo,String key,Future<? super Void> future){
        tools.handleFutureResult(key, new NonResultLocalRunner() {
            @Override
            public void task() {
                if (future.isSuccess()){

                    sendMessageInSerialize(key,tools);

                }else {
                    channelFutureMap.remove(key);
                }
            }
        });

    }
    private static void sendAuthMessage(ConnectTools tools,ChannelInfo channelInfo,String key){
        ChannelFuture channelFuture = tools.sendAuthMessage(channelInfo.getChannelFuture());
//        channelInfo.setSendAuth(true);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
               if (future.isSuccess()){
                   sendMessageInSerialize(key,tools);
               }
            }
        });

    }
    /**
     * 顺序发送消息
     * @param key
     */
    public static void sendMessageInSerialize(String key,ConnectTools tools){
        LinkedBlockingQueue queue = messageQueue.get(key);
        Iterator<GameMessage> iterator = queue.iterator();
        ChannelInfo channelInfo = channelFutureMap.get(key);
        if (channelInfo == null || channelInfo.getChannelFuture() == null){
            ChannelFuture channelFuture = tools.onlyConnectServer(channelInfo.getHost(), channelInfo.getPort(),channelFutureMap);
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    handleFuture(future,key,tools);
                }
            });
            return;
        }
        while (iterator.hasNext()){
            try {
                channelInfo.getChannelFuture().channel().writeAndFlush(iterator.next()).sync().addListener(
                        new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                if (future.isSuccess()){
//                                        System.out.println("send mesage to des------------");
                                }else {
                                    System.out.println("write message error "+future.cause());
                                    channelFutureMap.remove(key);
                                }
                            }
                        }
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iterator.remove();
//                    channelInfo.getChannelFuture().channel().flush();

        }

    }

}
