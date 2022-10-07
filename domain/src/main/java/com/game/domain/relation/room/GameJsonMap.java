package com.game.domain.relation.room;

import com.alibaba.fastjson.JSONObject;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class GameJsonMap {
    public static Map<Integer, SoftReference<JSONObject>> softReferenceMap = new HashMap<>();
    public static JSONObject getByGameType(int gameType){
        if (softReferenceMap.get(gameType) == null){
            return null;
        }
        return softReferenceMap.get(gameType).get();
    }
    public static void putInfo(Integer gameType, JSONObject jsonObject){
        softReferenceMap.put(gameType,new SoftReference<JSONObject>(jsonObject));
    }

    public static Map<Integer, SoftReference<RuleInfo>> softReferenceGameRuleMap = new HashMap<>();
    public static RuleInfo getGameRuleByGameType(int gameType){
        if (softReferenceMap.get(gameType) == null){
            return null;
        }
        return softReferenceGameRuleMap.get(gameType).get();
    }
    public static void putInfo(Integer gameType, RuleInfo ruleInfo){
        softReferenceGameRuleMap.put(gameType,new SoftReference<RuleInfo>(ruleInfo));
    }
}
