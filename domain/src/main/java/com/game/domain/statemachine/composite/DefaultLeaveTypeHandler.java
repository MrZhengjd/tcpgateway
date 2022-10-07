
package com.game.domain.statemachine;

import com.game.domain.statemachine.composite.IStatus;
import com.game.domain.statemachine.composite.LeaveRequest;
import com.game.domain.statemachine.composite.LeaveTypeHandler;
import com.game.domain.statemachine.composite.Rank;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class DefaultLeaveTypeHandler implements LeaveTypeHandler {

    private static Map<Integer, Rank> rankMap = new HashMap<>();
    static {
        rankMap.put(1,new  Rank("领导",1));
        rankMap.put(2,new  Rank("hr",2));
        rankMap.put(3,new  Rank("ceo",3));
        rankMap.put(4,new  Rank("success",4));
        rankMap.put(5,new  Rank("failed",5));
    }
    public static  Rank getByRan(int id){
        return rankMap.get(id);
    }
    @Override
    public Rank handleLeaveType(IStatus iStatus, LeaveRequest leaveRequest) {

        if (iStatus ==  IStatus.PERMIT_AGREE){
            int nextRand = leaveRequest.getRank().getRank() + 1;
            if (nextRand > 3){
                nextRand = 4;
            }
            if (rankMap.containsKey(nextRand)){
                return rankMap.get(nextRand);
            }
            throw new  RuntimeException("cannot support this");
        }
        else if (iStatus == IStatus.PERMIT_DISAGREE){
            return rankMap.get(5);

        }
        return leaveRequest.getRank();

    }
}
