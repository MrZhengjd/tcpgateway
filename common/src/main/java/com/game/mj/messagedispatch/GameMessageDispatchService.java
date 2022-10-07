package com.game.mj.messagedispatch;

import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.eventdispatch.ListenerHandler;
import com.game.mj.model.GameMessage;
import com.game.mj.model.HeaderAnno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个指令对应一个handler
 * @author zheng
 */
@Service
public class GameMessageDispatchService {
    private static Logger logger = LoggerFactory.getLogger(GameMessageDispatchService.class);
    private Map<String, List<ListenerHandler>> eventMapping = new HashMap<>();
    private Map<String, Map<String ,ListenerHandler>> nameEventMapping = new HashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    private Map<String ,Class> eventMap = new HashMap<>();
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @PostConstruct
    public void init(){
        init(applicationContext);
        init2(applicationContext);
    }
    public void init(ApplicationContext context){
        context.getBeansWithAnnotation(GameDispatchService.class).values().forEach(bean->{
            Method[] methods = bean.getClass().getMethods();
            String baseName = bean.getClass().getSimpleName();
            for (Method method : methods){
                GameMessageListener listener = method.getAnnotation(GameMessageListener.class);
                if (listener != null && listener.onUsed().equals("true")){
                    Class<? extends GameMessage> eventClass = listener.value();
                    HeaderAnno annotation = eventClass.getAnnotation(HeaderAnno.class);
                    if (annotation != null ){
                        ListenerHandler mapping = new ListenerHandler(bean,method);
                        String base =String.valueOf(annotation.serviceId())+"-"+annotation.messageType().value;
                        String temp = base;
                        if (!listener.name().equals("")){
                            temp =base+"-"+listener.name();
                        }
                        addEventListenerMapping(base,mapping,temp);
                        eventMap.put(eventClass.getName(),listener.value());
                    }


                }
            }
        });
    }
    public void init2(ApplicationContext context){
        context.getBeansWithAnnotation(GameDispatchService.class).values().forEach(bean->{
            Method[] methods = bean.getClass().getMethods();
            String baseName = bean.getClass().getSimpleName();
            for (Method method : methods){
                GameMessageDispatch listener = method.getAnnotation(GameMessageDispatch.class);
                if (listener != null && listener.onUsed().equals("true")){
                    HeaderAnno eventClass = listener.value();

                    if (eventClass != null ){
                        ListenerHandler mapping = new ListenerHandler(bean,method);
                        String base =String.valueOf(eventClass.serviceId())+"-"+eventClass.messageType().value;
                        String temp = base;
                        if (!listener.name().equals("")){
                            temp =eventClass.serviceId()+"-"+listener.name();
                        }
                        addEventListenerMapping(base,mapping,temp);
//                        eventMap.put(eventClass.getName(),listener.value());
                    }


                }
            }
        });
    }
    private  ListenerHandler handler(GameMessage organ, String desc){
        String  tem = String.valueOf(organ.getHeader().getServiceId()+"-"+organ.getHeader().getType());
        if (desc != null && !desc.equals("")){
            tem= tem+"-"+desc;
        }
        Map<String, ListenerHandler> gameTypeMap = nameEventMapping.get(tem);
        if (gameTypeMap == null){
            throw new RuntimeException("don't have correct response handler"+organ.getHeader().getServiceId() + " type "+organ.getHeader().getType());
        }
        return gameTypeMap.get(tem);

    }
    public void sendGameMessage(GameMessage gameMessage) throws InvocationTargetException, IllegalAccessException {

        sendGameMessage(gameMessage,gameMessage.getHeader().getDescribe());
    }
    public void sendGameMessage(GameMessage gameMessage, String desc) throws InvocationTargetException, IllegalAccessException {
        ListenerHandler handler = handler(gameMessage,desc);
        if (handler == null){
            throw new RuntimeException("cannot find the method with event name "+gameMessage+" method "+desc);
        }
        try {
            handler.getMethod().invoke(handler.getBean(),gameMessage);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("error here "+gameMessage.getHeader().getServiceId());
        }

    }
    public void sendGameMessage(GameMessage gameMessage, String desc,Class data) throws InvocationTargetException, IllegalAccessException {
        ListenerHandler handler = handler(gameMessage,desc);
        if (handler == null){
            throw new RuntimeException("cannot find the method with event name "+gameMessage+" method "+desc);
        }
//        Object o = gameMessage.deserialzeToData(data);
        handler.getMethod().invoke(handler.getBean(),gameMessage.deserialzeToData(data));
    }
    /**
     * 添加注册事件
     * @param baseName
     * @param mapping
     * @param annotation
     */
    private void addEventListenerMapping(String baseName, ListenerHandler mapping, String extendName) {

        List<ListenerHandler> listenerMappings = eventMapping.get(baseName);

        if (listenerMappings == null){
            listenerMappings = new ArrayList<>();
            eventMapping.put(baseName,listenerMappings);
        }
        listenerMappings.add(mapping);
        Map<String, ListenerHandler> localMap;

        if (nameEventMapping.containsKey(baseName)) {
            localMap =nameEventMapping.get(baseName);
        }else {
            localMap = new HashMap<>();
        }
        if (localMap.containsKey(extendName)){
            throw new RuntimeException("命名重复"+extendName);
        }
        localMap.put(extendName,mapping);
        nameEventMapping.put(extendName,localMap);
    }
}
