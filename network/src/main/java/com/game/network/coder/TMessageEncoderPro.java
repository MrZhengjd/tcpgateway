package com.game.network.coder;




import com.game.mj.constant.Constants;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.mj.model.GameMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

//@ChannelHandler.Sharable
public class TMessageEncoderPro extends MessageToByteEncoder<GameMessage> {

    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();

    public TMessageEncoderPro() {
    }

    public TMessageEncoderPro(DataSerialize dataSerialize) {
        this.dataSerialize = dataSerialize;
    }

    /**
     * @param ctx
//     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, GameMessage msg, ByteBuf out) throws Exception {
//        TMessage msg = message.buildTMessage();
//        THeader header = msg.getTHeader();
        try {
            out.writeInt(Constants.HEAD_START);
            byte[] headData = dataSerialize.serialize(msg.getHeader());
//        byte[] bodyData = dataSerialize.serialize(msg.getData());
            out.writeInt(headData.length);
            out.writeInt(msg.getData().length);

            out.writeInt(Constants.END_TAIL);
            out.writeBytes(headData);
            out.writeBytes(msg.getData());

        }catch (Exception e){
            e.printStackTrace();
            out.release();
        }


    }

}
