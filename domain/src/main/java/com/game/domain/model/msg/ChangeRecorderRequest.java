package com.game.domain.model.msg;

import com.game.mj.constant.RequestMessageType;
import com.game.mj.model.DefaultGameMessage;
import com.game.mj.model.HeaderAnno;
import com.game.mj.model.MessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.CHANGE_CORDER,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class ChangeRecorderRequest extends DefaultGameMessage {

//    private ChangeVo changeVo;
}
