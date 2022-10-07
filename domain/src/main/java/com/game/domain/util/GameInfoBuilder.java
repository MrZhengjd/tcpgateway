package com.game.domain.util;


import com.alibaba.fastjson.JSONObject;
import com.game.mj.util.GameJsonTxtMap;
import com.game.mj.util.TxtUtil;
import com.game.domain.relation.room.GameJsonMap;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class GameInfoBuilder {


    public static JSONObject getFromGameType(int gameType) {
        JSONObject jsonObject = GameJsonMap.getByGameType(gameType);
        if (jsonObject == null) {
            jsonObject = buildFromText(gameType);
            if (jsonObject == null) {
                throw new NullPointerException("cannot build the game rule");
            }
            GameJsonMap.putInfo(gameType, jsonObject);
        }
        return jsonObject;
    }

    private static JSONObject buildFromText(int gameType) {
        String jsonPath = GameJsonTxtMap.getJsonName(gameType);
        if (!StringUtils.isEmpty(jsonPath)) {
            return TxtUtil.loadTxt(jsonPath);
        }
        return null;
    }

    /**
     * 通过json来创建房间规则
     *
     * @param object
     * @return
     */
    public static void buildGameInfoFromJson(JSONObject object) {


//        GameInfo gameInfo = (GameInfo) JSONObject.toBean(object,GameInfo.class);
////        gameInfo.putAll(data);
//        List<CheckInfo> checkInfos = buildCheckInfos();
//        GameBaseInfo gameBaseInfo = new GameBaseInfo();
//        for (CheckInfo checkInfo : checkInfos){
//            checkInfo.checkInfo(object,gameBaseInfo);
//        }
//
////        GameInfoManager.putScoreInfo(object.getInt("gameType"),gameInfo);
//        GameBaseInfoManager.putGameBaseInfo(object.getInt("gameType"),gameBaseInfo);

    }












    public static Map<String, Object> buildInfo() {
        Map<String, Object> huInfo = new HashMap<>();
        huInfo.putAll(putHuInfos("serverPair"));
        return huInfo;
    }

    private static Map<String, Object> putHuInfos(String... infos) {
        Map<String, Object> huInfo = new HashMap<>();
        for (String info : infos) {
            huInfo.put(info, true);
        }
        return huInfo;
    }


}
