package com.game.mj.eventdispatch;


import com.game.mj.model.*;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zheng
 */
@Service
public class DynamicRegisterGameService {
    private Map<String ,Class<? extends GameMessage>> gameHoldMap = new HashMap<>();
//    private static class Holder{
//        private static DynamicRegisterGameService INSTANCE = new DynamicRegisterGameService();
//    }
    @Autowired
    private ConfigurableApplicationContext context;

//    private DynamicRegisterGameService() {
//    }
//
//    public static DynamicRegisterGameService getInstance(){
//        return DynamicRegisterGameService.Holder.INSTANCE;
//    }
    @PostConstruct
    public void init(){

        Reflections reflections = new Reflections("com.game");
        Set<Class<? extends AbstractGameMessage>> classSet = reflections.getSubTypesOf(AbstractGameMessage.class);
        classSet.forEach(c -> {
            HeaderAnno headerAnno = c.getAnnotation(HeaderAnno.class);
            if (headerAnno != null) {
                this.checkGameMessageMetadata(headerAnno, c);
                int serviceId = headerAnno.serviceId();
                MessageType mesasageType = headerAnno.messageType();
                String key = this.getMessageClassCacheKey(mesasageType, serviceId);
                gameHoldMap.put(key, c);
            }
        });




    }
    public GameMessage getResponseByMessageIdSimple(THeader header){

        return getResponseByMessageIdFromOldHeader(header.getServiceId(),header);
    }
    public GameMessage getDefaultRequest(Integer serviceId){
        GameMessage gameMessage = new DefaultGameMessage();
        gameMessage.getHeader().setType(MessageType.RPCREQUEST.value);
        gameMessage.getHeader().setServiceId(serviceId);
        return gameMessage;
    }
    public GameMessage getDefaultResponse(Integer serviceId){
        GameMessage gameMessage = new DefaultGameMessage();
        gameMessage.getHeader().setType(MessageType.RPCRESPONSE.value);
        gameMessage.getHeader().setServiceId(serviceId);
        return gameMessage;
    }
    /**
     * ?????????????????????????????????????????????????????????playerId
     * @param header
     * @param playerId
     * @return
     */
    public GameMessage getResponseToPlayerIdSimple(THeader header, Long playerId){

        GameMessage data = getResponseByMessageIdFromOldHeader(header.getServiceId(), header);
        data.getHeader().setPlayerId(playerId);
        return data;
    }

    /**
     * ?????????????????????????????????????????????????????????playerId
     * @param header
     * @param playerId
     * @return
     */
    public GameMessage getResponseToPlayerIdsSimple(THeader header, List<Long> playerIds){

        GameMessage data = getResponseByMessageIdFromOldHeader(header.getServiceId(), header);
        data.getHeader().setToPlayerIds(playerIds);
        data.getHeader().setSendWay(MessageSendType.ONE_BY_MANY.getValue());
        return data;
    }
    /**
     * ???????????????????????????????????????
     * @param messageId
     * @param old
     * @return
     */
    public GameMessage getResponseByMessageIdFromOldHeader(int messageId, THeader old){
        GameMessage gameMessage = getResponseInstanceByMessageId(messageId);
        gameMessage.copyHeadData(old);
        return gameMessage;
    }
    //??????????????????????????????
    public GameMessage getResponseInstanceByMessageId(int messageId) {
        return this.getMessageInstance(MessageType.RPCRESPONSE, messageId);
    }
    //??????????????????????????????
    public GameMessage getRequestInstanceByMessageId(int messageId) {
        return this.getMessageInstance(MessageType.RPCREQUEST, messageId);
    }
    //??????????????????????????????????????????
    public  GameMessage getMessageInstance(MessageType messageType,int messageId) {
        String key = this.getMessageClassCacheKey(messageType, messageId);
        Class<? extends GameMessage> clazz = this.gameHoldMap.get(key);
        if (clazz == null) {
            this.throwMetadataException("?????????messageId:" + key + "???????????????????????????Class");
        }
        GameMessage gameMessage = null;
        try {
            gameMessage = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String msg = "???????????????????????????," + "messageId:" + key + ", class:" + clazz.getName();
//            logger.error(msg, e);
            this.throwMetadataException(msg);
        }
        gameMessage.getHeader().setServiceId(messageId);
        gameMessage.getHeader().setType(messageType.value);
        return gameMessage;
    }
    private String getMessageClassCacheKey(MessageType type, int messageId) {
        return messageId + ":" + type.name();
    }
    //??????????????????????????????
    private void checkGameMessageMetadata(HeaderAnno headerAnno, Class<?> c) {
        int serviceId1 = headerAnno.serviceId();
        if (serviceId1 == 0) {
            this.throwMetadataException("messageId?????????:" + c.getName());
        }
        int serviceId = headerAnno.serviceId();
        if (serviceId == 0) {
            this.throwMetadataException("serviceId????????????" + c.getName());
        }
        MessageType mesasageType = headerAnno.messageType();
        if (mesasageType == null) {
            this.throwMetadataException("messageType?????????:" + c.getName());
        }

    }
    private void throwMetadataException(String msg) {
        throw new IllegalArgumentException(msg);
    }
}
