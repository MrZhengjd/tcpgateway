package com.game.domain.relation.organ;


/**
 * @author zheng
 */
public class ChuPaiOrgan implements Organ {
    private int chuPaiCount;
    @Override
    public void reset() {
        this.chuPaiCount = 0;
    }

    public int getChuPaiCount() {
        return chuPaiCount;
    }

    public void setChuPaiCount(int chuPaiCount) {
        this.chuPaiCount = chuPaiCount;
    }
}
