package com.game.domain.statemachine.solder;

import com.game.domain.statemachine.solder.Play;
import com.game.domain.statemachine.solder.SecondHandle;
import com.game.domain.statemachine.solder.StateChange;

/**
 * @author zheng
 */
public class DefaultSecondHandle implements SecondHandle {
    @Override
    public void handEnter( StateChange stateChange,  Play play) {
        System.out.println("second handle enter-------");
    }

    @Override
    public void handSwitch( StateChange stateChange,  Play play) {
        System.out.println("second handle switch-------");
    }

    @Override
    public void handleExit(StateChange stateChange, Play play) {
        System.out.println("second handle exit-------");
    }
}
