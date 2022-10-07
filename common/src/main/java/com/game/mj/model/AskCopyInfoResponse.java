package com.game.mj.model;

import com.game.mj.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ASK_COPY_ID,messageType = MessageType.RPCRESPONSE)
@Getter
@Setter
public class AskCopyInfoResponse extends DefaultGameMessage {

}
