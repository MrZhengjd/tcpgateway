package com.game.diststore.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;

/**
 * @author zheng
 */
@Getter
@Setter
public class SelectInfo {
//    private String selectKey;
    private CountDownLatch latch;
    private int status ;
}
