package com.game.diststore.role;

import com.game.mj.cache.FastChannelInfo;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.util.TopicUtil;
import com.game.diststore.server.GameServerBoot;
import com.game.diststore.service.RecordComponent;
import com.game.mj.messagedispatch.GameMessageDispatchService;
import com.game.network.cache.ChannelMap;
import com.game.network.server.SuccessHandle;
import io.netty.channel.ChannelInitializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zheng
 */
@Getter
@Setter
public abstract class BaseRole {
    @Autowired
    protected RecordComponent recordComponent;
    @Autowired
    protected DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    protected ChannelMap channelMap;

    @Autowired
    protected GameMessageDispatchService dispatchService;
    @Autowired
    protected GameServerBoot nettyServer;
    protected boolean isMaster;
    protected void startGame(String host, Integer port, ChannelInitializer channelInitializer){
        nettyServer.startServer(host,port,channelInitializer);
    }
    protected void startGame(String host, Integer port, ChannelInitializer channelInitializer,SuccessHandle successHandle){
        nettyServer.startServer(host,port,channelInitializer,successHandle);
    }
    public void startGame(String host, Integer port){
        try {
            FastChannelInfo.setChannelInfo(TopicUtil.generateTopic(host,port));
            startGame(host,port,generate());

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void startGame(String host, Integer port, SuccessHandle successHandle){
        try {
            FastChannelInfo.setChannelInfo(TopicUtil.generateTopic(host,port));
            startGame(host,port,generate(),successHandle);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public abstract ChannelInitializer generate();
}
