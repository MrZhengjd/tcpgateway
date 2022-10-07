package com.game.domain.statemachine;

import com.game.domain.statemachine.AnnualStatusHandler;
import com.game.domain.statemachine.LeavePermitType;
import com.game.domain.statemachine.Status;
import com.game.domain.statemachine.StatusHander;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jetty on 18/1/9.
 */
public class StatusHandlerRegistry {

    private static Map<String,  StatusHander> statusHandlerMap;

    static {
        statusHandlerMap=new ConcurrentHashMap<String,  StatusHander>();
        registryStatusHandler( LeavePermitType.ANNUAL_LEAVE,  Status.PERMIT_SUBMIT,new AnnualStatusHandler());
    }

    private StatusHandlerRegistry(){

    }

    private static String getKey( LeavePermitType leavePermitType,  Status status){
        return String.format("%s@-@%s",leavePermitType.getType(),status.name());
    }

    /**
     * 注册状态处理类
     * @param leavePermitType  请假类型
     * @param status           请假状态
     * @param statusHandler    状态处理对象
     */
    public static void registryStatusHandler( LeavePermitType leavePermitType,  Status status,  StatusHander statusHandler){
        statusHandlerMap.put(getKey(leavePermitType,status),statusHandler);
    }

    /**
     * 获取状态处理类
     * @param leavePermitType  请假类型
     * @param status            请假状态
     * @return StatusHandler
     */
    public static StatusHander acquireStatusHandler(LeavePermitType leavePermitType, Status status){
        return statusHandlerMap.get(getKey(leavePermitType,status));
    }

}
