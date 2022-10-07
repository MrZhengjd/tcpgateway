package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.Play;
import com.game.domain.statemachine.solder.StateChange;

/**
 * @author zheng
 */
public interface SecondHandle {
    public void handEnter( StateChange stateChange,  Play play);

    public void handSwitch( StateChange stateChange,  Play play);

    public void handleExit(StateChange stateChange, Play play);
}
