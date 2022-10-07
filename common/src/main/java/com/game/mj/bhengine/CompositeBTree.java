package com.game.mj.bhengine;


import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
public interface CompositeBTree extends BTree {
    void addBTree(CompositeBTree bTree);
    void removeTree(CompositeBTree bTree);
    List<CompositeBTree> getChildrens();
    EStatus handleRequest(Map<String, Object> request);
}
