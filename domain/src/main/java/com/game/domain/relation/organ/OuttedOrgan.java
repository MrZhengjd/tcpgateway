package com.game.domain.relation.organ;

import com.game.domain.relation.pai.Pai;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class OuttedOrgan implements Organ {
    private List<Pai> outtedPaiList = new ArrayList<>();



    public List<Pai> getOuttedPaiList() {
        return outtedPaiList;
    }

    public void setOuttedPaiList(List<Pai> outtedPaiList) {
        this.outtedPaiList = outtedPaiList;
    }

    @Override
    public void reset() {
        outtedPaiList.clear();
    }
}
