package com.game.mj.bhengine;


import java.util.Map;

/**
 * @author zheng
 */
public interface BHEngine {
    void handleRequest(Map<String, Object> name, RootTree rootTree);
}
