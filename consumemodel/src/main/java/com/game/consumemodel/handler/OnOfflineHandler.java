package com.game.consumemodel.handler;

import com.game.mj.constant.InfoConstant;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.*;
import com.game.mj.util.TopicUtil;
import com.game.consumemodel.config.CommonConsumeConfig;
import com.game.consumemodel.find.FindPlayerIdProxy;
//import com.game.domain.consume.SendMessageModel;
import com.game.domain.consumer.SendMessageModel;
import com.game.mj.messagedispatch.GameDispatchService;
import com.game.mj.messagedispatch.GameMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 上下线处理器
 * @author zheng
 */
@GameDispatchService
@Service
public class OnOfflineHandler {
    private static Logger logger = LoggerFactory.getLogger(OnOfflineHandle.class);
    @Autowired
    private SendMessageModel sendMessageModel;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;

    @Autowired
    private CommonConsumeConfig commonConsumeConfig;


    @Autowired
    private FindPlayerIdProxy findPlayerIdProxy;
    @GameMessageListener(value = OnlineMessageRequest.class,name = "323")
    public void handleOnline(OnlineMessageRequest onlineMessageRequest){

        handleOnOffline(commonConsumeConfig.getOnlineSendWay(), onlineMessageRequest.getHeader());
        logger.info("here is handle online message ");
    }

    @GameMessageListener(value = OfflineMessageRequest.class,name = "323")
    public void handleOffline(OfflineMessageRequest offlineMessageRequest){
        handleOnOffline(commonConsumeConfig.getOfflineSendWay(), offlineMessageRequest.getHeader());
    }

    /**
     * 处理并返回上下线消息
     * @param fingPlayerIdsWay
     * @param tHeader
     */
    private void handleOnOffline(int fingPlayerIdsWay, THeader tHeader){
        List<Long> playerIds = findPlayerIdProxy.findPlayerIds(fingPlayerIdsWay, tHeader.getPlayerId());
        if (playerIds == null || playerIds.isEmpty()){
            playerIds = new ArrayList<>();
            playerIds.add(tHeader.getPlayerId());
        }
        GameMessage response = dynamicRegisterGameService.getResponseToPlayerIdsSimple(tHeader,playerIds);
        response.setMessageData("welcome");
        String topic = TopicUtil.generateTopic(InfoConstant.GATEWAY_LOGIC_TOPIC, tHeader.getTraceId());// 动态创建与业务服务交互的消息总线Topic
        sendMessageModel.sendMessageToMq(response, topic,response.getHeader().getAttribute());
    }
}
