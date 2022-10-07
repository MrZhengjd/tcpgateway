package com.game.mj.bhengine;

/**
 * @author zheng
 */
public class DefaultCompositeTree extends AbstractCompositeTree {

    public DefaultCompositeTree(boolean type) {
        super(type);
    }

    public DefaultCompositeTree() {
    }

    public DefaultCompositeTree(SelectorEnum selectorEnum, boolean type) {
        super(selectorEnum, type);
    }

    public DefaultCompositeTree(SelectorEnum selectorEnum, int calType, Object meetValue) {
        super(selectorEnum, calType, meetValue);
    }

    public DefaultCompositeTree(SelectorEnum selectorEnum, int calType, Object meetValue, String name) {
        super(selectorEnum, calType, meetValue, name);
    }

    public DefaultCompositeTree(int calType, Object meetValue, String name, boolean type) {
        super(calType, meetValue, name, type);
    }


}
