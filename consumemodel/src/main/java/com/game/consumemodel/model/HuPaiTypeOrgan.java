package com.game.consumemodel.model;

import com.game.domain.relation.organ.Organ;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class HuPaiTypeOrgan implements Organ {
    private Map<String ,Boolean> huPaiTypeMap = new HashMap<>();
    @Override
    public void reset() {
        huPaiTypeMap.clear();
    }
}
