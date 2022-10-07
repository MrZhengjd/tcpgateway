package com.game.domain.relation.pai;

import java.io.Serializable;

/**
 * @author zheng
 */
public class Pai implements Serializable {
    private int paiId;
    private int paiType;

    public int getPaiType() {
        return paiType;
    }

    public void setPaiType(int paiType) {
        this.paiType = paiType;
    }

    public int getPaiId() {
        return paiId;
    }

    public void setPaiId(int paiId) {
        this.paiId = paiId;
    }
}
