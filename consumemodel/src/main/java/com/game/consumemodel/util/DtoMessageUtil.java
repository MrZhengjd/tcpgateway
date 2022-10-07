package com.game.consumemodel.util;

import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.GameMessage;
import com.game.mj.model.THeader;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author zheng
 */
public class DtoMessageUtil {
    private static DataSerialize dataSerialize =  DataSerializeFactory.getInstance().getDefaultDataSerialize();
    public static GameMessage readMessageHeader(byte[] datas, DynamicRegisterGameService dynamicRegisterGameService){

        ByteBuf byteBuf = Unpooled.wrappedBuffer(datas);
        int headerLength = byteBuf.readInt();
        int dataLength = byteBuf.readInt();
        byte[] head = new byte[headerLength];
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(head);
        byteBuf.readBytes(data);
        THeader h =dataSerialize.deserialize(head, THeader.class);
        GameMessage gameMessage = dynamicRegisterGameService.getRequestInstanceByMessageId(h.getServiceId());
        gameMessage.setHeader(h);
        gameMessage.readBody(data);
        return gameMessage;
    }
}
