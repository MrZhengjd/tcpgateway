package com.game.mj.engine;

import java.util.Map;

public class RuleNodeComposite extends RuleNode {
    private String desc;
    //计算方式 ；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围]
    private int calType;
    protected SelectorEnum selectType = SelectorEnum.Selector;
    public SelectorEnum getSelectType() {
        return selectType;
    }

    public void setSelectType(SelectorEnum selectType) {
        this.selectType = selectType;
    }
    //值
    private Object limitVal;

    private Map<String ,Long> toRuleMap;

    public Map<String, Long> getToRuleMap() {
        return toRuleMap;
    }

    public void setToRuleMap(Map<String, Long> toRuleMap) {
        this.toRuleMap = toRuleMap;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public int getCalType() {
        return calType;
    }

    public void setCalType(int calType) {
        this.calType = calType;
    }



    public Object getLimitVal() {
        return limitVal;
    }

    public void setLimitVal(Object limitVal) {
        this.limitVal = limitVal;
    }
}
