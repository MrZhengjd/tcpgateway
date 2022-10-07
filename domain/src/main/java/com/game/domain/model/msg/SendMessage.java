package com.game.domain.model.msg;


public class SendMessage  {

    private String fromUser;
    private String toUser;
    private Object message;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }



    @Override
    public String toString() {
        return "SendMessage{" +
                "fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", message=" + message +
                '}';
    }
}
