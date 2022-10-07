package com.game.mj.bhengine;


/**
 * @author zheng
 */
public class BehaviorTreeBuilder {
//    private Stack<CompositeBTree> behaviorStack = new Stack<>();
    private CompositeBTree operate = null;
    private CompositeBTree last = null;
    private RootTree root;
    public BehaviorTreeBuilder addBehavior(CompositeBTree behavior){
        if (root == null){
            root = new RootTree(behavior);
            operate = behavior;
        }else {
            operate.addBTree(behavior);
        }
//        behaviorStack.push(behavior);
        return this;
    }

    public BehaviorTreeBuilder addAndTurnTo(CompositeBTree behavior){
        if (root == null){
            root = new RootTree(behavior);
        }else {
            operate.addBTree(behavior);
        }
        last = operate;
        operate = behavior;
//        behaviorStack.push(behavior);
        return this;
    }
    public BehaviorTreeBuilder back(){
        operate = last;
        return this;
    }
    public RootTree rootTree(){
//        behaviorStack.clear();
        return root;
    }
}
