package com.game.domain.relation.organ;

import com.game.mj.eventdispatch.Event;

/**
 * @author zheng
 */
public  interface Organ extends Event {
//    private OrganAction organAction;
//    private Object input;
//
//    public Object getInput() {
//        return input;
//    }
//
//    public void setInput(Object input) {
//        this.input = input;
//    }

//    protected Organ() {
//    }

    public  void reset();
//    protected Organ(OrganAction organAction) {
//        this.organAction = organAction;
//    }
//    public void operate(){
//        if (organAction == null){
//            return;
//        }
//        organAction.organOperate(this);
//    }
}
