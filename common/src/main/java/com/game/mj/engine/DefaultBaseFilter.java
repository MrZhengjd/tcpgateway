package com.game.mj.engine;

public  class DefaultBaseFilter implements BaseFilter{
    @Override
    public Long filter(RuleNodeComposite composite,Object filterValue){

        int index = FilterUtil.decisionLogic(filterValue,composite);
        return composite.getToRuleMap().get(composite.getKey() +index);

    }
}
