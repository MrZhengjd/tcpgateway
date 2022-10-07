package com.game.mj.eventdispatch;

/**
 * @author zheng
 */
public class DisPlayEvent implements Event {
    private String name;

    public DisPlayEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name "+name;
    }
}
