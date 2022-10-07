package com.game.mj.engine;

import java.util.HashMap;
import java.util.Map;

public class FilterContext {
    private static Map<String ,BaseFilter> filterMap = new HashMap<>();
    static {
        filterMap.put("common",new DefaultBaseFilter());
    }
    private FilterContext(){}
    public static FilterContext getInstance(){
        return FilterInstance.INSTANCE;
    }
    private static class FilterInstance{
        private static FilterContext INSTANCE = new FilterContext();
    }
    public BaseFilter getFilterByKey(String key){
        return filterMap.get(key);
    }
}
