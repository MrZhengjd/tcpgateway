package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.EquitmentState;
import com.game.domain.statemachine.solder.PlayState;
import com.game.domain.statemachine.solder.StateChange;
import com.game.domain.statemachine.solder.StateType;

import java.util.Stack;

/**
 * @author zheng
 */
public class Play {
    private StateChange stateChange;
    private Stack<PlayState> changeStack;
    private Stack<EquitmentState> preEquitMentState;
    private EquitmentState equitmenState;

    public Stack<EquitmentState> getPreEquitMentState() {
        return preEquitMentState;
    }

    public void setPreEquitMentState(Stack<EquitmentState> preEquitMentState) {
        this.preEquitMentState = preEquitMentState;
    }

    public EquitmentState getEquitmenState() {
        return equitmenState;
    }

    public void setEquitmenState(EquitmentState equitmenState) {
        this.equitmenState = equitmenState;
    }

    public Stack<PlayState> getChangeStack() {
        return changeStack;
    }

    public void setChangeStack(Stack<PlayState> changeStack) {
        this.changeStack = changeStack;
    }

    private StateType stateType;
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public StateChange getStateChange() {
        return stateChange;
    }

    public void setStateChange(StateChange stateChange) {
        this.stateChange = stateChange;
    }

    public Play(StateChange stateChange) {
        this.stateChange = stateChange;
    }

    public Play(StateType stateType) {
        this.stateChange = StateChange.None;
        this.stateType = stateType;
    }
}
