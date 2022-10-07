package com.game.consumemodel.model;

//import com.game.common.model.MessageType;
//import com.game.common.model.anno.DefaultGameMessage;
//import com.game.common.model.anno.HeaderAnno;
//import com.game.common.model.msg.BaseChuPaiInfo;
//import com.game.common.model.msg.RequestMessageType;
//import lombok.Getter;
import com.game.mj.model.DefaultGameMessage;
import com.game.domain.model.msg.BaseChuPaiInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
public class BackRoomMessage extends DefaultGameMessage {
    private BaseChuPaiInfo baseChuPaiInfo;
}
