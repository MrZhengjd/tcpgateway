package com.game.mj.model;


import com.game.mj.constant.RequestMessageType;

/**
 * @author zheng
 */

@HeaderAnno(serviceId = RequestMessageType.OFFLINE_MESSAGE,messageType = MessageType.RPCRESPONSE)
public class OfflineMessageResponse extends DefaultGameMessage {

}
