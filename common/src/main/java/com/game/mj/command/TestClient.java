package com.game.mj.command;

/**
 * @author zheng
 */
public class TestClient {
    public static void main(String[] args) {
        ChatMediator chatMediator = new ChatMediatorImpl();
        User user = new UserImpl(chatMediator,"pi");
        User user1 = new UserImpl(chatMediator,"pi1");
        User user2 = new UserImpl(chatMediator,"pi2");
        User user3 = new UserImpl(chatMediator,"pi3");
        chatMediator.addUser(user);
        chatMediator.addUser(user1);
        chatMediator.addUser(user2);
        chatMediator.addUser(user3);

        user1.send("hello test");
    }
}
