package com.game.mj.model;

import com.game.mj.constant.RequestMessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.AUTH_REQUEST,messageType = MessageType.RPCRESPONSE)
public class AuthMessageResponse extends DefaultResponseGameMessage{
}
