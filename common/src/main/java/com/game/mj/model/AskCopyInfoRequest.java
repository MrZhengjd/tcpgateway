package com.game.mj.model;

import com.game.mj.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ASK_COPY_ID,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class AskCopyInfoRequest extends DefaultGameMessage {
}
