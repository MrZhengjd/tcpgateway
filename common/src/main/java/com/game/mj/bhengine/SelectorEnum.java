package com.game.mj.bhengine;

/**
 * @author zheng
 */
public enum SelectorEnum {
    //selector 有一个成功就可以了
    //sequence 一次执行，只要有一个不对就返回
    //所有都同时执行
    Selector,Sequence,Parallel;
}
