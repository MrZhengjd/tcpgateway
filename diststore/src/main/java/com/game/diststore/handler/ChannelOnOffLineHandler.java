package com.game.diststore.handler;

import com.game.mj.eventdispatch.EventDispatchService;
import com.game.diststore.service.RecordComponent;
import com.game.network.cache.ChannelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@EventDispatchService
@Component
public class ChannelOnOffLineHandler {
    @Autowired
    private ChannelMap channelMap;
    @Autowired
    private RecordComponent component;
//    @EventListenerAnnotation(value =  OfflineEvent.class)
//    public void processOfflie(Object object, OfflineEvent offlineEvent){
//        this.channelMap.removeChannel(offlineEvent.getAddress());
//        this.component.removeAddress(offlineEvent.getAddress());
//    }
//    @EventListenerAnnotation(value =  OnlineEvent.class)
//    public void processOnlie(Object object, OnlineEvent onlineEvent){
//        this.channelMap.addChannel(onlineEvent.getAddress(),onlineEvent.getChannel());
//        this.component.addAddressToNodeMap(onlineEvent.getAddress());
//    }
}
