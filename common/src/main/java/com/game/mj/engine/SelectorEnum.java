package com.game.mj.engine;

/**
 * @author zheng
 */
public enum SelectorEnum {
    //selectro 选择一个执行
    //sequence 一次执行，只要有一个不对就返回
    //所有都同时执行
    Selector,Sequence,Parallel;
}
