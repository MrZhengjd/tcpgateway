package com.game.mj.exception;

import java.io.Serializable;

public class TokenException extends Exception implements Serializable {
    private boolean expired;

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);

    }

    public TokenException(boolean expired) {
        this.expired = expired;
    }
}
