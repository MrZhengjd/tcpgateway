package com.game.domain.relation.role;


import com.game.mj.serialize.PbSerializeUtil;
import com.game.domain.relation.organ.*;
import com.game.domain.relation.room.GameRule;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class PlayerRoleManager extends RoleManager<PlayerRole> {
    public static void addOrgan(Map<String, Organ> organMap, Organ organ){
        if (organMap == null){
            return;
        }
        organMap.put(organ.getClass().getSimpleName(),organ);
    }
    public static PlayerRole defaultPlayerRole(){
        OutterOrgan outterOrgan = new OutterOrgan();
        InnerOrgan innerOrgan = new InnerOrgan();
        OuttedOrgan outtedOrgan = new OuttedOrgan();
        HandOrgan handOrgan = new HandOrgan();
        return buildWithOrgan(outtedOrgan,outterOrgan,innerOrgan,handOrgan);
    }
    public static PlayerRole buildPlayerRole(){
        OutterOrgan outterOrgan = new OutterOrgan();
        InnerOrgan innerOrgan = new InnerOrgan();
        OuttedOrgan outtedOrgan = new OuttedOrgan();
//         ChuPaiAction chuPaiAction = new ChuPaiAction();
        Map<String, Organ> organMap = new HashMap<>();
        addOrgan(organMap,outtedOrgan);
        addOrgan(organMap,outterOrgan);
        addOrgan(organMap,innerOrgan);
        addOrgan(organMap,new HandOrgan());
//         actionMap.put("chuPaiAction",chuPaiAction);
        return new PlayerRole(organMap);
    }
    private static class PlayerRoleBuilder{
        private PlayerRole playerRole =buildPlayerRole();
        public PlayerRole build(){
            return playerRole;
        }

    }
    private static PlayerRole buildWithOrgan(Organ... organs){
        Map<String, Organ> organMap = new HashMap<>();
        for (Organ organ : organs){
            addOrgan(organMap,organ);
        }
        return new PlayerRole(organMap);
    }
//    public void executeRule(ExecuteRule executeRule, RuleInfo ruleInfo, String name){
//        if (ruleInfo.getRuleInfo().containsKey(name) ){
//            executeRule.executeRule();
//        }
//    }
    public Organ getOrganFromSimpleName(Class<? extends Organ> organ){
        return getRole().organMap.get(organ.getSimpleName());
    }

    public <T> T getOrganFromName(Class<? extends Organ> organ){
        return (T)getRole().organMap.get(organ.getSimpleName());
    }

    public <T extends Organ> T getOrganIfNullCreateIt(Class<T> organClass){
        T organFromSimpleName = (T) getOrganFromSimpleName(organClass);
        if (organFromSimpleName == null){
            organFromSimpleName = PbSerializeUtil.newInstance(organClass);
            addOrgan(organFromSimpleName);
        }
        return organFromSimpleName;
    }
    public PlayerRoleManager(PlayerRole playerRole){
        super(playerRole);
    }
    public  void addOrgan(Organ organ){
        if (getRole().getOrganMap() == null){
            return;
        }

        getRole().organMap.put(organ.getClass().getSimpleName(),organ);
    }
    public void putHuPaiInfo(String name,Boolean value){
        getRole().getHuPaiInfo().put(name,value);
    }
    public void calScore(GameRule gameRule){
        Double score = 0d;
        for (Map.Entry<String,Object> checkInfoEntry : getRole().getCheckInfo().entrySet()){
            if (gameRule.getScoreMap().containsKey(checkInfoEntry.getKey())){
                score += gameRule.getScoreMap().get(checkInfoEntry.getKey());
            }
        }
        (getRole()).setCurrentRoundScore(score);
    }
}
