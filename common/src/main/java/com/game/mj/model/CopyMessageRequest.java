package com.game.mj.model;

import com.game.mj.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.COPY_DATA,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class CopyMessageRequest extends DefaultGameMessage {

//    @Override
//    protected Class getBodyObjClass() {
//        return CopyM.class;
//    }
}
