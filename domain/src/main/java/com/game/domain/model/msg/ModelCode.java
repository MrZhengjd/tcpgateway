package com.game.domain.model.msg;

/**
 * @author zheng
 */
public enum ModelCode {
    SUCCESS(200),FAILED(404);
    private int code;

    ModelCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
