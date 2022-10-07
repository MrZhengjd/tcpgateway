package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.HandleStateChange;
import com.game.domain.statemachine.solder.Play;
import com.game.domain.statemachine.solder.PlayState;
import com.game.domain.statemachine.solder.StateChange;

/**
 * @author zheng
 */
public class SolderStateChangeHandler implements HandleStateChange {

    @Override
    public void enterState( StateChange stateChange,  Play play) {
        System.out.println("solder enter -------");
        play.getChangeStack().push(PlayState.Move);
    }

    @Override
    public void switchState( StateChange stateChange,  Play play) {
        System.out.println("solder switch");
    }

    @Override
    public void exitState( StateChange stateChange,  Play play) {
        System.out.println("solder exist");
        play.getChangeStack().pop();
    }

    @Override
    public void noneState(StateChange stateChange, Play play) {
        System.out.println("solder none state");
    }
}
