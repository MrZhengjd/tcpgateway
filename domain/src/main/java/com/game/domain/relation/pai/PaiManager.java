package com.game.domain.relation.pai;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class PaiManager {
    transient
    private static List<Pai> totalPaiList = new ArrayList<>();
    static {
        for (int i = 11;i< 58 ;i++){
            if (i % 10 == 0){
                continue;
            }
            Pai pai = new Pai();
            pai.setPaiId(i);
            totalPaiList.add(pai);
        }
    }
    public static List<Pai> getTotalPaiList(){
        return totalPaiList;
    }

}
