package com.game.domain.statemachine;

import com.game.domain.statemachine.LeavePermit;
import com.game.domain.statemachine.StatusHander;
import com.game.domain.statemachine.StatusHandlerRegistry;
import com.game.domain.statemachine.composite.DefaultStateEventHandler;
import com.game.domain.statemachine.composite.StateEventHandler;

/**
 * @author zheng
 */
public class Engine {
    private LeavePermit leavePermit;

    public Engine(LeavePermit leavePermit) {
        this.leavePermit = leavePermit;
    }

    /**
     * 处理的流程是一个permitType 加一个status绑定一个handler
     * 所以他就是n*m个方法的叠加
     * 正确的办法是两个分开来处理
     */
    public void process(){
        StatusHander statusHander = StatusHandlerRegistry.acquireStatusHandler(leavePermit.getLeavePermitType(), leavePermit.getStatus());
        statusHander.handle(leavePermit);
    }


}
