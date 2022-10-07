package com.game.mj.util;

/**
 * @author zheng
 */
public class TopicUtil {
    public static String generateTopic(String prefix,int serverId) {
        return prefix + "-" + serverId;
    }

    public static String generateTopic(Integer prefix,int serverId) {
        return prefix + "-" + serverId;
    }
}
