package com.game.domain.checkhu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
@Component
public class CheckHuProxy {
    @Autowired
    private ApplicationContext applicationContext;
    private Map<String , ExecuteCheckHu> executeRuleMap = new HashMap<>();
    @PostConstruct
    public void init(){
        Map<String, ExecuteCheckHu> beansOfType = applicationContext.getBeansOfType(ExecuteCheckHu.class);
        for (Map.Entry<String, ExecuteCheckHu> entry :beansOfType.entrySet()){
            executeRuleMap.put(entry.getValue().getClass().getSimpleName(),entry.getValue());
        }
    }
    public ExecuteCheckHu getByKet(String key){
        return executeRuleMap.get(key);
    }

}
