package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.HandleStateChange;
import com.game.domain.statemachine.solder.Play;
import com.game.domain.statemachine.solder.StateChange;

/**
 * @author zheng
 */
public class BaseStateChange implements HandleStateChange {


    @Override
    public void enterState( StateChange stateChange,  Play play) {

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
