package com.game.mj.eventdispatch;

//import com.game.common.flow.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
@Service
public class EventAnnotationManager {
    private Map<String, List<ListenerHandler>> eventMapping = new HashMap<>();
    private Map<String, Map<String ,ListenerHandler>> nameEventMapping = new HashMap<>();
    private Map<String ,Class> eventMap = new HashMap<>();
    @Autowired
    private ApplicationContext context;

//    private static class Holder{
//        private static EventAnnotationManager INSTANCE = new EventAnnotationManager();
//    }
//    public static EventAnnotationManager getInstance(){
//        return Holder.INSTANCE;
//    }
    @PostConstruct
    public void init(){
        init(context);
    }
    /**
     * 初始化自动注册到map里面去
     * @param context
     */
    public void init(ApplicationContext context){
        context.getBeansWithAnnotation(EventDispatchService.class).values().forEach(bean->{
            Method[] methods = bean.getClass().getMethods();
            String baseName = bean.getClass().getSimpleName();
            for (Method method : methods){
                EventListenerAnnotation listener = method.getAnnotation(EventListenerAnnotation.class);
                if (listener != null){
                    Class<? extends Event> eventClass = listener.value();
                    ListenerHandler mapping = new ListenerHandler(bean,method);
                    addEventListenerMapping(eventClass.getName(),mapping,baseName+"-"+method.getName());
                    eventMap.put(eventClass.getName(),listener.value());
                }
            }
        });
    }


    /**
     * 添加注册事件
     * @param baseName
     * @param mapping
     */
    private void addEventListenerMapping(String baseName, ListenerHandler mapping,String extendName) {

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
        if (localMap.containsKey(baseName+"-"+extendName)){
            throw new RuntimeException("命名重复");
        }
        localMap.put(extendName,mapping);
        nameEventMapping.put(baseName,localMap);
    }

    /**
     * 发送事件
     * @param event
     * @param origin
     */
    public void sendEvent(Event event,Object origin){
        String key = event.getClass().getName();
        List<ListenerHandler> mappings = eventMapping.get(key);
        if (mappings != null){
            mappings.forEach(mapping->{
                try {
                    mapping.getMethod().invoke(mapping.getBean(),origin,event);
                }catch (Exception e){
                    e.printStackTrace();

                }
            });
        }

    }
//    public void sendPlayerEvent(String eventName, Node node, PlayerRequest event){
//        if (node == null){
//            return;
//        }
//        Map<String, ListenerHandler> gameTypeMap = nameEventMapping.get(eventName);
//        ListenerHandler handler = gameTypeMap.get(node.getDesc());
//        if (handler == null){
//            throw new RuntimeException("cannot find the method with event name "+eventName+" method "+node.getDesc());
//        }
//        try {
//
//            handler.getMethod().invoke(handler.getBean(),event);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void sendPlayerRoleEvent(Organ organ, String desc){
//        ListenerHandler handler = handler(organ,desc);
//        if (handler == null){
//            throw new RuntimeException("cannot find the method with event name "+organ+" method "+desc);
//        }
//        try {
//            handler.getMethod().invoke(handler.getBean(),organ);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private  ListenerHandler handler(Organ organ, String desc){
//        Map<String, ListenerHandler> gameTypeMap = nameEventMapping.get(organ.getClass().getName());
//        return gameTypeMap.get(desc);
//
//    }
//    public void sendPlayerRoleWithArgue(Organ organ, String desc,Object data){
//        ListenerHandler handler = handler(organ,desc);
//        if (handler == null){
//            throw new RuntimeException("cannot find the method with event name "+organ+" method "+desc);
//        }
//        try {
////            organ.setInput(data);
//            handler.getMethod().invoke(handler.getBean(),organ,data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendPlayerRoleWithArgues(Organ organ, String desc,Object... data){
//        ListenerHandler handler = handler(organ,desc);
//        if (handler == null){
//            throw new RuntimeException("cannot find the method with event name "+organ+" method "+desc);
//        }
//        try {
////            organ.setInput(data);
//            handler.getMethod().invoke(handler.getBean(),organ,data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
