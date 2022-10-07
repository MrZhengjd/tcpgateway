package com.game.mj.model;


/**
 * @author zheng
 */
public interface GameMessage<T> {
    byte[] getData();
    void setHeader(THeader tHeader);
    void readBody(byte[] data);
    void setMessageData(T body);
    void readHeader(byte[] header);
    void copyHeadData(THeader tHeader);
    T deserialzeToData(Class<T> t);
    THeader getHeader();

}
