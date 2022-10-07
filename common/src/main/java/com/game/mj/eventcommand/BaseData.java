package com.game.mj.eventcommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseData {
    private Long id;
    private String test;


    @Override
    public String toString() {
        return "id "+id +" test "+ test;
    }
}
