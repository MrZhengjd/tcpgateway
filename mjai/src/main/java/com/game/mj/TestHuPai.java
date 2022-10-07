package com.game.mj;

import com.game.domain.relation.role.PlayerRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
public class TestHuPai {
    public static void main(String[] args) {
        PlayerRole playerRole = PlayerRole.getInstance();
        List<Integer> paiList = new ArrayList<>();
//        for (int i = 0;i< 3;i++){
//            for (int j = 0;j< 3;j++){
//                int key = (i+1)* 10 + j +1;
//                paiList.add(key);
//            }
//        }
//        paiList.add(11);
        paiList.add(11);
        paiList.add(11);
        paiList.add(11);
        paiList.add(12);
        paiList.add(13);
        paiList.add(14);
        paiList.add(16);
        paiList.add(17);
        paiList.add(18);

        paiList.add(41);
        paiList.add(41);
        paiList.add(41);
        paiList.add(43);
        paiList.add(43);

        HoldPaiInfo holdPaiInfos = PaiPool.getHoldPaisFromIds(paiList);
        playerRole.getOperateInfo().put("holdPaiInfos",holdPaiInfos);
        Map<Integer, List<Integer>> integerListMap = TransferPaiUtil.transferHoldPaiInfo(holdPaiInfos);
        for (Map.Entry<Integer, List<Integer>> entry : integerListMap.entrySet()){
            System.out.println("key "+entry.getKey() + " value "+entry.getValue());
        }

    }
}
