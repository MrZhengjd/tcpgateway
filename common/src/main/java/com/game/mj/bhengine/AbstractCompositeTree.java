package com.game.mj.bhengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
public abstract class AbstractCompositeTree extends BaseBTree implements CompositeBTree {

    private static Map<SelectorEnum,ChildHandleWay> handleWayMap = new HashMap<>();
    static {
        handleWayMap.put(SelectorEnum.Selector,new SelectorChildHandleWay());
        handleWayMap.put(SelectorEnum.Sequence,new SequenceChildHandleWay());
        handleWayMap.put(SelectorEnum.Parallel,new ParallelChildHandleWay());
    }

    public AbstractCompositeTree() {
    }
    protected AbstractCompositeTree(SelectorEnum selectorEnum,boolean type){
        super(selectorEnum,type);
    }

    public AbstractCompositeTree(boolean type) {
        super(type);
    }


    protected AbstractCompositeTree(SelectorEnum selectorEnum, int calType, Object meetValue) {
        this.selectorEnum = selectorEnum;
        this.calType = calType;
        this.meetValue = meetValue;
    }

    protected AbstractCompositeTree(SelectorEnum selectorEnum, int calType, Object meetValue, String name) {
        this.selectorEnum = selectorEnum;
        this.calType = calType;
        this.meetValue = meetValue;
        this.name = name;
    }

    protected AbstractCompositeTree(int calType, Object meetValue, String name, boolean type) {
        this.calType = calType;
        this.meetValue = meetValue;
        this.name = name;
        this.type = type;
    }

    protected AbstractCompositeTree(int calType, Object meetValue) {
        this.calType = calType;
        this.meetValue = meetValue;
    }
    private List<CompositeBTree> children = new ArrayList<>();

    @Override
    public void addBTree(CompositeBTree bTree) {
        children.add(bTree);
    }

    @Override
    public void removeTree(CompositeBTree bTree) {
        children.remove(bTree);
    }

    @Override
    public List<CompositeBTree> getChildrens() {
        return children;
    }

    @Override
    public EStatus handleRequest(Map<String, Object> request) {
        return handleWayMap.get(selectorEnum).handle(children, request);

    }



    interface ChildHandleWay{
        EStatus handle(List<CompositeBTree> bTrees, Map<String, Object> request);
    }
    static class SequenceChildHandleWay implements ChildHandleWay{

        @Override
        public EStatus handle(List<CompositeBTree> bTrees,Map<String, Object> request) {
            for (CompositeBTree bTree : bTrees){
                if (bTree.handleRequest(request)==EStatus.Failure){
                    return EStatus.Failure;
                }
            }
            return EStatus.Success;
        }
    }
    static class SelectorChildHandleWay implements ChildHandleWay{

        @Override
        public EStatus handle(List<CompositeBTree> bTrees,Map<String, Object> request) {
            for (CompositeBTree bTree : bTrees){
                if (bTree.handleRequest(request)==EStatus.Success){
                    return EStatus.Success;
                }
            }
            return EStatus.Failure;
        }
    }
    static class ParallelChildHandleWay implements ChildHandleWay{

        @Override
        public EStatus handle(List<CompositeBTree> bTrees,Map<String, Object> request) {
            for (CompositeBTree bTree : bTrees){
                bTree.handleRequest(request);
            }
            return EStatus.Success;
        }
    }
}
