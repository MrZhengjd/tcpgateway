package com.game.domain.relation.room;

import com.game.mj.constant.InfoConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
@Getter
@Setter
public class RuleInfo {
//    private Map<String ,BaseRuleInfo> baseRuleInfoMap = new HashMap<>();
    private Map<String,Object> ruleInfo = new HashMap<>();
//
//
    public Map<String ,Double> getRuleCalScoreMap( String name){
        return (Map<String, Double>) ruleInfo.get(name);
    }
    public List<String> getRuleList(String name){
        return getValue(name);
    }

    /**
     * 获取beforeHu
     * @return
     */
    public List<String> getBeforeHus(){
        return getRuleList(InfoConstant.BEFORE_HU_LIST);
    }
    /**
     * 获取checkhu
     * @return
     */
    public List<String> getCheckHus(){
        return getRuleList(InfoConstant.CHECK_HU_LIST);
    }

    public <T> T getValue(String name){
        if (ruleInfo.containsKey(name)){
            return (T)ruleInfo.get(name);
        }
        return null;
    }
    /**
     * 获取AfterHus
     * @return
     */
    public List<String> getAfterHus(){
        return getRuleList(InfoConstant.AFTER_HU_LIST);
    }

    public Boolean getBuildRule(String name){
        return  getValue(name);
    }

    public Integer getBuileValue(String name){
        return  getValue(name);
    }
    public Double getBuileValueDouble(String name){
        return  getValue(name);
    }
    public Long getBuileValueDoubleLong(String name){
        return  getValue(name);
    }
}
