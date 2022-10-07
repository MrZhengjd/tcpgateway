package com.game.domain.relation.organ;


import com.game.domain.relation.pai.Pai;

/**
 * @author zheng
 */
public class HandOrgan implements Organ {
    private Pai moPai;

    public HandOrgan() {
    }



    @Override
    public void reset() {
        moPai = null;
    }

    public HandOrgan(Pai moPai) {
        this.moPai = moPai;
    }

    public Pai getMoPai() {
        return moPai;
    }

    public void setMoPai(Pai moPai) {
        this.moPai = moPai;
    }
}
