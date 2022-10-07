package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.HandleStateChange;
import com.game.domain.statemachine.solder.Play;
import com.game.domain.statemachine.solder.SecondHandle;
import com.game.domain.statemachine.solder.StateChange;

/**
 * @author zheng
 */
public class StateChangeProxy implements HandleStateChange {
    private HandleStateChange handleStateChange;
    private  SecondHandle secondHandle;
    private HandleStateChange equitMentHandle;

    public StateChangeProxy(HandleStateChange handleStateChange,  SecondHandle secondHandle, HandleStateChange equitMentHandle) {
        this.handleStateChange = handleStateChange;
        this.secondHandle = secondHandle;
        this.equitMentHandle = equitMentHandle;
    }

    public HandleStateChange getHandleStateChange() {
        return handleStateChange;
    }

    public void setHandleStateChange(HandleStateChange handleStateChange) {
        this.handleStateChange = handleStateChange;
    }

    public  SecondHandle getSecondHandle() {
        return secondHandle;
    }

    public void setSecondHandle( SecondHandle secondHandle) {
        this.secondHandle = secondHandle;
    }

    public HandleStateChange getEquitMentHandle() {
        return equitMentHandle;
    }

    public void setEquitMentHandle(HandleStateChange equitMentHandle) {
        this.equitMentHandle = equitMentHandle;
    }

    public StateChangeProxy(HandleStateChange handleStateChange, SecondHandle secondHandle) {
        this.handleStateChange = handleStateChange;
        this.secondHandle = secondHandle;
    }

    public StateChangeProxy(HandleStateChange baseStateChange) {
        this.handleStateChange = baseStateChange;
    }


    @Override
    public void enterState( StateChange stateChange,  Play play) {
        handleStateChange.enterState(stateChange,play);
        if (secondHandle != null){
            secondHandle.handEnter(stateChange,play);
        }
        if (equitMentHandle != null){
            equitMentHandle.enterState(stateChange,play);
        }
    }

    @Override
    public void switchState( StateChange stateChange,  Play play) {
        handleStateChange.switchState(stateChange,play);
        if (secondHandle != null){
            secondHandle.handSwitch(stateChange,play);
        }
    }

    @Override
    public void exitState( StateChange stateChange,  Play play) {
        handleStateChange.exitState(stateChange,play);
        if (secondHandle != null){
            secondHandle.handleExit(stateChange,play);
        }
    }

    @Override
    public void noneState(StateChange stateChange, Play play) {
        handleStateChange.noneState(stateChange,play);
    }
}
