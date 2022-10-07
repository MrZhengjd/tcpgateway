package com.game.mj.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class ChatMediatorImpl implements ChatMediator {
    private List<User> users;
    @Override
    public void sendMessage(String msg, User user) {
        for (User user1 : users){
            if (user != user1){
                user1.receive(msg);
            }
        }
    }

    public ChatMediatorImpl() {
        this.users = new ArrayList<>();
    }

    @Override
    public void addUser(User user) {
        this.users.add(user);
    }
}
