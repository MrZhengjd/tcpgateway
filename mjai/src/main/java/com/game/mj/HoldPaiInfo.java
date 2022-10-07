package com.game.mj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoldPaiInfo {

    private List<HPai> holdPais;
    private List<Map<String ,Object>> holdInfo;

    public HoldPaiInfo(List<HPai> holdPais) {
        this.holdPais = holdPais;
        this.holdInfo = new ArrayList<>();
    }
}
