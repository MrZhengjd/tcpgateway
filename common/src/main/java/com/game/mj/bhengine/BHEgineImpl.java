package com.game.mj.bhengine;


import java.util.Map;

/**
 * @author zheng
 */
public class BHEgineImpl implements BHEngine {


    @Override
    public void handleRequest(Map<String, Object> request, RootTree rootTree) {
       rootTree.handleRequest(request);
    }
}
