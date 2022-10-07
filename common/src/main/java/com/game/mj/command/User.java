package com.game.mj.command;

/**
 * @author zheng
 */
public abstract class User {
    protected ChatMediator mediator;
    protected String name;

    public User(ChatMediator mediator,String name){
        this.mediator = mediator;
        this.name = name;
    }
    abstract void send(String msg);
    abstract void receive(String msg);
}
