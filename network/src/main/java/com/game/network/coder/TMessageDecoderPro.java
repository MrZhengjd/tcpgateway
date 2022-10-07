package com.game.network.coder;


import com.game.mj.cache.MasterInfo;
import com.game.mj.constant.Constants;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.*;
import com.game.mj.serialize.*;
import com.game.mj.util.IncommingCount;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//@ChannelHandler.Sharable
public class TMessageDecoderPro extends ByteToMessageDecoder {
    private static Logger logger = LoggerFactory.getLogger(TMessageDecoderPro.class);
    private DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    private final static int HEADER_LENGTH = 16;
    private final static int MAX_LENGTH = 65535;
    private DynamicRegisterGameService dynamicRegisterGameService;

//    private static MasterI
    public TMessageDecoderPro(DynamicRegisterGameService dynamicRegisterGameService) {
        this.dynamicRegisterGameService = dynamicRegisterGameService;
    }

    public TMessageDecoderPro() {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (in.readableBytes() < HEADER_LENGTH) {
                in.clear();
                return;
            }
            if (in.readableBytes() > MAX_LENGTH) {
                in.skipBytes(in.readableBytes());
                return;
            }
            int beginReader;
            while (true) {
                beginReader = in.readerIndex();
                in.markReaderIndex();
                if (in.readInt() == Constants.HEAD_START) {
                    break;
                }

                in.resetReaderIndex();
                in.readByte();

                if (in.readableBytes() < HEADER_LENGTH) {
                    return;
                }
            }
            int headLength = in.readInt();
            int bodyLength = in.readInt();

            int endData = in.readInt();
            if (endData != Constants. END_TAIL){
                logger.error("receive data not write");
                return;
            }
//
            if (in.readableBytes() < headLength+bodyLength)
            {
                in.resetReaderIndex();
                return;
            }


            byte[] header = new byte[headLength];
            in.readBytes(header);
            byte[] data = new byte[bodyLength];
            in.readBytes(data);
//
            THeader tHeader = serializeUtil.deserialize(header, THeader.class);

            GameMessage gameMessage = null;
            if (dynamicRegisterGameService != null  && tHeader.getServiceId() < RequestMessageType.ASK_MASTER_CHANGE){
                gameMessage = dynamicRegisterGameService.getMessageInstance(MessageType.fromName(tHeader.getType()),tHeader.getServiceId());
            }else {
                gameMessage = new DefaultGameMessage();
            }
            gameMessage.setHeader(tHeader);
            if (!MasterInfo.checkIsMasterOrRecorder() && !RequestMessageType.checkIsCopyOrAuth(tHeader.getServiceId()) && !MasterInfo.checkIsRecorder() && !MessageType.checkIsAuthAndResponse(tHeader.getType()) ){
                GameMessage response = dynamicRegisterGameService.getResponseInstanceByMessageId(tHeader.getServiceId());
                response.setMessageData(ResponseVo.fail("not open"));
                ctx.channel().writeAndFlush(response);
                System.out.println("cannot challllllllll");
                return;
            }
            gameMessage.readBody(data);
            out.add(gameMessage);
            IncommingCount.getAndIncrementDecode();
//            in.resetReaderIndex();
        } catch (Exception e) {
            logger.error("decode error " + e,e);
        }
    }


//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        if (in.readableBytes() < HEADER_LENGTH) {
//            in.clear();
//            return;
//        }
//        if (in.readableBytes() > MAX_LENGTH) {
//            in.skipBytes(in.readableBytes());
//            return;
//        }
//        byte[] body = new byte[in.readableBytes()];
//        out.add(serializeUtil.deserialize(body, MessageVo.class));
//    }
}
