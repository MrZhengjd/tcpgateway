package com.game.domain.checkhu;

import com.alibaba.fastjson.JSONObject;
//import com.game.common.relation.RuleInfo;
import com.game.domain.relation.room.RuleInfo;
import com.game.domain.util.GameInfoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
@Component
public class DefaultCheckHuPools {

    @Autowired
    private CheckHuProxy checkHuProxy;

    private Map<Integer, DefaultCheckHu> checkHuMap = new HashMap<>();
    private Map<Integer, RuleInfo> ruleInfoMap = new HashMap<>();
    public DefaultCheckHu getCheckHuModel(int gameType){
        if (checkHuMap.get(gameType) == null){
            initDefaultCheckHu(gameType);
        }
        return checkHuMap.get(gameType);
    }

    /**
     * 初始化检测胡牌实现类
     * @param gameType
     */
    private void initDefaultCheckHu(int gameType) {
        JSONObject jsonObject = GameInfoBuilder.getFromGameType(gameType);
        RuleInfo ruleInfo = jsonObject.toJavaObject(RuleInfo.class);
        ruleInfoMap.put(gameType,ruleInfo);
        DefaultCheckHu checkHu = new DefaultCheckHu();
//      
        addData(ruleInfo.getBeforeHus(),checkHu.getBeforeHus());
        addData(ruleInfo.getCheckHus(),checkHu.getCheckHuses());
        addData(ruleInfo.getAfterHus(),checkHu.getAfterHus());
        ruleInfoMap.put(gameType,ruleInfo);
    }
    private void addData(List<String> datas,List<ExecuteCheckHu> hold){
        if (datas == null || datas.isEmpty()){
            return;
        }
        for (String s :datas){
            hold.add(checkHuProxy.getByKet(s));
        }
    }


}
