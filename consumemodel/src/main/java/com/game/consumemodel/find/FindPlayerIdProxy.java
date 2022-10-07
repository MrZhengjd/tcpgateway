package com.game.consumemodel.find;

import com.game.mj.annotation.BandKey;
import com.game.mj.constant.InfoConstant;
import com.game.mj.model.THeader;
import com.game.domain.find.FindPlayerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.game.common.model.msg.THeader;

/**
 * @author zheng
 */
@Component
public class FindPlayerIdProxy {
//    private Map<Stri>
    private Map<String,FindPlayerId> findPlayerIdMap = new HashMap<>();
//    @Autowired
//    @Qualifier("findRoomPlayerId")
//    private FindPlayerId findRoomPlayerId;
    @Autowired
    private ApplicationContext context;
    @PostConstruct
    public void init(){
        Map<String, FindPlayerId> beansOfType = context.getBeansOfType(FindPlayerId.class);
        for (FindPlayerId findPlayerId : beansOfType.values()){
            BandKey annotation = findPlayerId.getClass().getAnnotation(BandKey.class);
            if (annotation != null){
                findPlayerIdMap.put(InfoConstant.FIND_PLAYERID_PREFIX+annotation.key(),findPlayerId);
            }
        }
//        findPlayerIdMap.put(MessageSendType.ONE_BY_MANY.value,findRoomPlayerId);
    }
    public FindPlayerId getFindRoomPlayerId(Integer key){
        return findPlayerIdMap.get(InfoConstant.FIND_PLAYERID_PREFIX+key);
    }
    public List<Long> findPlayerIdsByHeader(THeader header){
        return findPlayerIds(header.getSendWay(),header.getAttribute());
    }
    public List<Long> findPlayerIds(Integer key,Object findKey){
        FindPlayerId findRoomPlayerId = getFindRoomPlayerId(key);
        if (findRoomPlayerId == null){
           return null;
        }
        return findRoomPlayerId.findPlayerIds(findKey);
    }
}
