package com.game.mj.model;

import com.game.mj.constant.RequestMessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.COPY_DATA,messageType = MessageType.RPCRESPONSE)
public class CopyMessageResponse extends DefaultResponseGameMessage {
}
