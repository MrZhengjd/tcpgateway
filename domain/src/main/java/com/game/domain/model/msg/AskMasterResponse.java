package com.game.domain.model.msg;

import com.game.mj.constant.RequestMessageType;
import com.game.mj.model.DefaultResponseGameMessage;
import com.game.mj.model.HeaderAnno;
import com.game.mj.model.MessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ASK_MASTER,messageType = MessageType.RPCRESPONSE)
@Getter
@Setter
public class AskMasterResponse extends DefaultResponseGameMessage {
}
