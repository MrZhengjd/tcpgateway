package com.game.network.cache;


import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.cache.ReturnOperate;
//import com.game.common.model.vo.PlayerChannel;
import com.game.mj.model.GameMessage;
import com.game.mj.model.PlayerChannel;
import com.game.mj.util.TopicUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
@Service
public class ChannelMap {
    private Map<Long, PlayerChannel> playerChannelMap = new HashMap<>();
    private Map<String ,PlayerChannel> channelMap = new HashMap<>();
    private Map<String,Channel> hostChannelMap = new HashMap<>();
    ReadWriteLockOperate lockUtil = new ReadWriteLockOperate();
    private void readLock(Operation operation){
        ReadWriteLockOperate lockUtil = new ReadWriteLockOperate();
        lockUtil.readLockOperation(operation);
    }

    public PlayerChannel getByPlayerId(Long playerId){

        return lockUtil.readLockReturnOperation( new ReturnOperate<PlayerChannel>() {
            @Override
            public PlayerChannel operate() {
                return playerChannelMap.get(playerId);
            }
        });
    }
    public PlayerChannel getByChannel(Channel channel){
        return lockUtil.writeLockReturnOperation(new ReturnOperate<PlayerChannel>() {
            @Override
            public PlayerChannel operate() {
                return channelMap.get(channel.id().asShortText());
            }
        });
    }
    public void addChannel(ChannelHandlerContext context){
        writeLock(new Operation() {
            @Override
            public void operate() {
                hostChannelMap.put(context.channel().id().asShortText(),context.channel());
            }
        });
    }
    public void addChannel(InetSocketAddress address, Channel channel){
       addChannel(address.getHostName(),address.getPort(),channel);

    }
    /**
     *
     * @param host
     * @param port
     * @param channel
     */
    public void addChannel(String host,Integer port,Channel channel){
        writeLock(new Operation() {
            @Override
            public void operate() {
                hostChannelMap.put(TopicUtil.generateTopic(host,port),channel);
            }
        });

    }
    public Channel getByHostPort(String host,Integer port){

        return lockUtil.readLockReturnOperation(new ReturnOperate<Channel>() {
            @Override
            public Channel operate() {
                return hostChannelMap.get(TopicUtil.generateTopic(host,port));
            }
        });
    }
    public void removeChannel(InetSocketAddress address){
        removeChannel(address.getHostName(),address.getPort());

    }
    public void removeChannel(String host,int port){
        writeLock(new Operation() {
            @Override
            public void operate() {
                hostChannelMap.remove(TopicUtil.generateTopic(host,port));
            }
        });
    }
    public void addChannel(Long playerId,Channel channel){
        writeLock(new Operation() {
            @Override
            public void operate() {
                PlayerChannel playerChannel = new PlayerChannel();
                playerChannel.setPlayerId(playerId);
                playerChannel.setChannel(channel);
                playerChannelMap.put(playerId,playerChannel);
                channelMap.put(channel.id().asShortText(),playerChannel);
            }
        });
    }

    public void removeChannel(String key){
        lockUtil.readLockOperation(new Operation() {
            @Override
            public void operate() {
                hostChannelMap.remove(key);
            }
        });
    }
    public void removeChannle(Long playerId){
        writeLock(new Operation() {
            @Override
            public void operate() {
                playerChannelMap.remove(playerId);
            }
        });
    }
    private void writeLock(Operation operation){
        ReadWriteLockOperate lockUtil = new ReadWriteLockOperate();
        lockUtil.writeLockOperation(operation);

    }
    public void writeResponse(GameMessage gameMessage){
        readLock(new Operation() {
            @Override
            public void operate() {
                PlayerChannel playerChannel = playerChannelMap.get(gameMessage.getHeader().getPlayerId());
                if (playerChannel!=null){
                    playerChannel.writeMessage(gameMessage);
                }else {
                    System.out.println("cannot found the channle" +gameMessage.getHeader().getPlayerId() + " service id"+gameMessage.getHeader().getServiceId() +" type "+ gameMessage.getHeader().getType());
                }

            }
        });
    }
}
