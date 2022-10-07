package com.game.domain.model.vo;


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
@NoArgsConstructor
@AllArgsConstructor
public class TMessageVo implements Serializable {
//    private int crcCode;
    private int serverId;
    private int headLength;
    private int bodyLength;
    private byte[] header;
    private byte[] data;



}
