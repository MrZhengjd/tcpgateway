package com.game.mj.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class FastChannelInfo {
    public static Map<String,String> channelContext = new HashMap<>();
    private static final String lostInfo = "localInfo";
    public static String getChannelInfo(){

        return channelContext.get(lostInfo);

    }

    public static void setChannelInfo(String info){

        channelContext.put(lostInfo,info);
    }

}
