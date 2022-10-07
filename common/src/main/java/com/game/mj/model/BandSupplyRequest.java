package com.game.mj.model;

import com.game.mj.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.BAND_SERVICE,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class BandSupplyRequest extends DefaultGameMessage {
//    private BandServerVo bandServerVo;

}
