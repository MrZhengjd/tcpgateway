package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.EquitmentState;
import com.game.domain.statemachine.solder.HandleStateChange;
import com.game.domain.statemachine.solder.Play;
import com.game.domain.statemachine.solder.StateChange;

/**
 * @author zheng
 */
public class EquitMentStateHandle implements HandleStateChange {
    @Override
    public void enterState( StateChange stateChange,  Play play) {
        if (stateChange ==  StateChange.Enter){
            play.getPreEquitMentState().push(EquitmentState.Attack);
            System.out.println("equitment state "+play.getPreEquitMentState().pop());
        }
    }

    @Override
    public void switchState( StateChange stateChange,  Play play) {

    }

    @Override
    public void exitState( StateChange stateChange,  Play play) {

    }

    @Override
    public void noneState(StateChange stateChange, Play play) {

    }
}
