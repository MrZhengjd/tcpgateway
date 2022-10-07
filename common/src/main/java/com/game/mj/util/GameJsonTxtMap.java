package com.game.mj.util;




import java.util.HashMap;
import java.util.Map;

public class GameJsonTxtMap {
    public static Map<Integer, String> jsonObjectMap = new HashMap<>();
    static {
        jsonObjectMap.put(361,"baoji");

    }
    public static String getJsonName(int gameType){
        return jsonObjectMap.get(gameType);
    }
}
