package com.game.mj;

import java.util.*;

/**
 * @author zheng
 */
public class PaiPool {
    private static Map<Integer,HPai> hPaiMap = new HashMap<>();
    private static List<HPai> paiPools = new ArrayList<>();
    static {
        for (int i = 10;i<58;i++){
            if (i % 10 == 0){
                continue;
            }
            if (i == 48 || i == 49){
                continue;
            }
            HPai pai = new HPai();
            pai.setNumber(i);
            pai.setType(Type.checkType(i));
            paiPools.add(pai);
            hPaiMap.put(i,pai);
        }
    }

    public static HPai getByKey(int key){
        return hPaiMap.get(key);
    }

    public static List<HPai> getPaiFromIds(List<Integer> keys){
        List<HPai> result = new ArrayList<>();
        Iterator<Integer> iterator = keys.iterator();
        while (iterator.hasNext()){
            Integer next = iterator.next();
            result.add(getByKey(next));
        }
        return result;
    }

    public static HoldPaiInfo getHoldPaisFromIds(List<Integer> keys){
        List<HPai> holdPais = getPaiFromIds(keys);
        return new HoldPaiInfo(holdPais);
    }
}
