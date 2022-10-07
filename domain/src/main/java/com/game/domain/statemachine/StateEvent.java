package com.game.domain.statemachine;

/**
 * @author zheng
 */
public enum StateEvent {
    AGREE("agree","同意"),
    DISAGREE("disagree","不同意"),
    MODIFY("modify","修改");

    StateEvent(String type, String memo) {
        this.type = type;
        this.memo = memo;
    }

    private String type;
    private String memo;
}
