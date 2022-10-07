package com.game.mj;

import java.util.*;

/**
 * @author zheng
 */
public class TransferPaiUtil {

    public static Map<Integer,List<Integer>> transferHoldPaiInfo(HoldPaiInfo holdPaiInfo) {
        Map<Integer,List<Integer>> result = new HashMap<>();
        Collections.sort(holdPaiInfo.getHoldPais());
        int lastOperateId = -99;
        Iterator<HPai> iterator = holdPaiInfo.getHoldPais().iterator();
        while (iterator.hasNext()){
            HPai next = iterator.next();
            List<Integer> list = checkAndSetData(result, next);
            if (list.isEmpty()){
                list.add(1);
            }else {
                if (next.getNumber() != lastOperateId){
                    if (next.getNumber() - lastOperateId == 1){
                        list.add(1);
                    }else {
                        for (int i = 0;i< next.getNumber()-lastOperateId-1;i++){
                            list.add(0);
                        }
                        list.add(1);
                    }
                }else {
                    Integer tmp = list.get(list.size() - 1);
                    tmp ++;
                    list.set(list.size() - 1,tmp);
                }


            }
            lastOperateId = next.getNumber();
        }
        return result;
    }

    private static List<Integer>  checkAndSetData(Map<Integer,List<Integer>> result,HPai hPai){
        int paiType = hPai.getType().index;

        List<Integer> holdData ;
        if (result.containsKey(paiType)){
            holdData = result.get(paiType);
        }else {
            holdData = new ArrayList<>();
            result.put(paiType,holdData);
        }
        return holdData;
    }
}
