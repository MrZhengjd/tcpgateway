package com.game.domain.model.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RModel implements Serializable {
    private int code;
    private Object data;
    private String msg;

    public static RModel success(Object data, String msg) {
        return new RModel(ModelCode.SUCCESS.getCode(),data,msg);

    }
}
