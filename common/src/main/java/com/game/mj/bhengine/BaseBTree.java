package com.game.mj.bhengine;


import java.util.Map;

/**
 * @author zheng
 */
public abstract class BaseBTree implements BTree {
//    protected CompositeBTree compositeBTree=new DefaultCompositeTree();
    protected SelectorEnum selectorEnum = SelectorEnum.Selector;
    //计算方式 ；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围]
    protected int calType;

    protected Object meetValue;

    protected String name;

    protected boolean type;

    public BaseBTree(boolean type) {
        this.type = type;
    }

    protected BaseBTree(SelectorEnum selectorEnum,boolean type) {
        this.selectorEnum = selectorEnum;
        this.type = type;
    }
//    @Override
//    public CompositeBTree getBtree() {
//        return compositeBTree;
//    }

    protected BaseBTree() {
    }



    public SelectorEnum getSelectorEnum() {
        return selectorEnum;
    }

    public void setSelectorEnum(SelectorEnum selectorEnum) {
        this.selectorEnum = selectorEnum;
    }


    @Override
    public EStatus handle(Map<String, Object> request) {
        if (type == true && !request.containsKey(name)){
            return EStatus.Failure;
        }
        return SimpleFilterUtil.checkMeet(request.get(name),calType, meetValue)==0?EStatus.Success : EStatus.Failure;
    }
}
