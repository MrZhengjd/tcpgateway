package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.Play;
import com.game.domain.statemachine.solder.StateChange;

/**
 * @author zheng
 */
public interface HandleStateChange {
    void enterState( StateChange stateChange,  Play play);
    void switchState( StateChange stateChange,  Play play);
    void exitState( StateChange stateChange,  Play play);
    void noneState(StateChange stateChange, Play play);
}
