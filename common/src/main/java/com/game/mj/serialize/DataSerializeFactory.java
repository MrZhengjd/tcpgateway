package com.game.mj.serialize;

import java.util.HashMap;
import java.util.Map;

public class DataSerializeFactory {
    private static final Map<String,DataSerialize> serializeMap = new HashMap<>();
    static {
        serializeMap.put("pbk",new PbSerializeUtil());
    }

    private static class InstanceHolder{
        static DataSerializeFactory instance = new DataSerializeFactory();
    }
    public static DataSerializeFactory getInstance(){
        return InstanceHolder.instance;
    }

    public  DataSerialize getDefaultDataSerialize(){
        return serializeMap.get(SerializeProtocol.PBK_PROTOCOL.getProtocol());
    }
}
