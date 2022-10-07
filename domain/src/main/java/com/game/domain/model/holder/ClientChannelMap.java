package com.game.domain.model.holder;


import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.cache.ReturnOperate;
import com.game.domain.model.vo.PlayerChannel;
import io.netty.channel.ChannelFuture;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class ClientChannelMap {
    private static Map<String , PlayerChannel> playerChannelMap = new HashMap<>();
    private static Map<String , ChannelFuture> channelMap = new HashMap<>();
    private static ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public static ChannelFuture getByKey(String key){
        return readWriteLockOperate.readLockReturnOperation(new ReturnOperate<ChannelFuture>() {
            @Override
            public ChannelFuture operate() {
                return channelMap.get(key);
            }
        });
    }

//    public static void rotateSendMessage(Message message){
//        readWriteLockOperate.readLockOperation(new Operation() {
//            @Override
//            public void operate() {
//                for (Map.Entry<String , ChannelFuture> entry : channelMap.entrySet()){
////                    message.getHeader().setTraceId(playerChannelMap.get(entry.getKey()).getPlayerId().intValue());
//                    ChannelFuture future = entry.getValue().channel().writeAndFlush(message);
//                    future.addListener(new GenericFutureListener<Future<? super Void>>() {
//                        @Override
//                        public void operationComplete(Future<? super Void> future) throws Exception {
//                            if (future.isSuccess()){
//                                IncommingCount.getAndIncrementSending();
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//    }
    public static void saveChannelFuture(String key, ChannelFuture channelFuture,Long playerId){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                PlayerChannel playerChannel = new PlayerChannel();
                playerChannel.setPlayerId(playerId);
                playerChannel.setChannel(channelFuture.channel());
                playerChannelMap.put(key,playerChannel);
                channelMap.put(key,channelFuture);
            }
        });
    }
}
