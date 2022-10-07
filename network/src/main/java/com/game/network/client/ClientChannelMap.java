package com.game.network.client;

import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.cache.ReturnOperate;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zheng
 */
@Component
public class ClientChannelMap {
    private  Map<String , ChannelFuture> channelMap = new HashMap<>();
    private  AtomicInteger count = new AtomicInteger(0);
    private  ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public  ChannelFuture getByKey(String key){
        return readWriteLockOperate.readLockReturnOperation(new ReturnOperate<ChannelFuture>() {
            @Override
            public ChannelFuture operate() {
                return channelMap.get(key);
            }
        });
    }
//    public  ClientChannelMap getInstance(){
//        return Holder.instance;
//    }
//    private ClientChannelMap(){}
//    private static class Holder{
//        private static ClientChannelMap instance = new ClientChannelMap();
//    }
    public static void batchWrite(Channel channel, Object message){
        for (int i = 0;i< 50;i++){
            channel.write(message);
        }
        channel.flush();


    }
    public  void saveChannelFuture(String key, ChannelFuture channelFuture){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                channelMap.put(key,channelFuture);
            }
        });
    }
}
