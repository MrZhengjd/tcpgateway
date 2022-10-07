package com.game.mj.model;


import com.game.mj.constant.RequestMessageType;

/**
 * @author zheng
 */
//@Component
@HeaderAnno(serviceId = RequestMessageType.ONLINE_MESSAGE,messageType = MessageType.RPCREQUEST)

public class OnlineMessageRequest extends DefaultGameMessage {

    public OnlineMessageRequest() {

//        System.out.println("herei -----------");
    }
}
