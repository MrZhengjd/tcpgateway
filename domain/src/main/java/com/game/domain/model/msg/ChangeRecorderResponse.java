package com.game.domain.model.msg;

import com.game.mj.constant.RequestMessageType;
import com.game.mj.model.DefaultGameMessage;
import com.game.mj.model.HeaderAnno;
import com.game.mj.model.MessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.CHANGE_CORDER,messageType = MessageType.RPCRESPONSE)
public class ChangeRecorderResponse extends DefaultGameMessage {

}
