package com.game.domain.model;



import com.game.mj.model.GameMessage;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.domain.model.vo.TMessageVo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author zheng
 */

public class DtoMessage {
    private static PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;




    private static DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();


    public DtoMessage(TMessageVo message) {
    }

    public static byte[] serializeData(GameMessage gameMessage){
        ByteBuf byteBuf = allocator.directBuffer();
        writeToByteBuf(byteBuf,serializeUtil,gameMessage);
        int length = byteBuf.readableBytes();
//        System.out.println("out readable bytes "+byteBuf.readableBytes());
        if (length > 0) {
//            System.out.println("here is coming ======================");
            byte[] datas = new byte[length];
            byteBuf.readBytes(datas);
            byteBuf.release();
            return datas;
        }
        byteBuf.release();
        return null;
    }


//    private static DtoMessage readFromByte(ByteBuf byteBuf) {
//        int headLength = byteBuf.readInt();
//        int dataLength = byteBuf.readInt();
//        byte[] head = byteBuf.readBytes(headLength).array();
//        byte[] data = byteBuf.readBytes(dataLength).array();
//        DtoHeader header = serializeUtil.deserialize(head, DtoHeader.class);
//        return new DtoMessage(header,data);
//    }

    private static void writeToByteBuf(ByteBuf byteBuf, DataSerialize dataSerialize,GameMessage gameMessage) {

        byte[] headData = dataSerialize.serialize(gameMessage.getHeader());
        byte[] bodyData = gameMessage.getData();
        byteBuf.writeInt(headData.length);
        byteBuf.writeInt(bodyData.length);
        byteBuf.writeBytes(headData);
        byteBuf.writeBytes(bodyData);
    }
}
