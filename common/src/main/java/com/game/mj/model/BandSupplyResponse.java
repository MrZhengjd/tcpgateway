package com.game.mj.model;

import com.game.mj.constant.RequestMessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.BAND_SERVICE,messageType = MessageType.RPCRESPONSE)
public class BandSupplyResponse extends DefaultGameMessage {
}
