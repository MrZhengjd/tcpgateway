package com.game.mj.serialize;

public enum SerializeProtocol {
    PBK_PROTOCOL("pbk");
    private String protocol;

    SerializeProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
