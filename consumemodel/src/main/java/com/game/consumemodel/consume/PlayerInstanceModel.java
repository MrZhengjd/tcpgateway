package com.game.consumemodel.consume;

import com.game.mj.model.ServerVo;
import com.game.domain.playerinstance.PlayerInstance;
import com.game.domain.registerservice.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
public class PlayerInstanceModel {
    @Autowired
    private RegisterService registerService;
    @Autowired
    @Qualifier("localPlayerInstance")
    private PlayerInstance localPlayerInstance;

    @Autowired
    @Qualifier("redisPlayerInstance")
    private PlayerInstance redisPlayerInstance;
    public PlayerInstanceModel() {
    }


    /**
     * 获得对应的module name
     * @param moduleId
     * @return
     */
    public String getModuleName(Integer moduleId){
        return registerService.getModuleName(moduleId);
    }

    public Integer selectServerId(Long playerId,Integer moduleId){
        return selectServerInfo(playerId, moduleId);


    }

    /**
     * 本地没有数据就到缓存去拉数据
     * @param playerId
     * @param moduleId
     */
    private Integer selectServerInfo(Long playerId,Integer moduleId){
        Integer serverId = localPlayerInstance.selectPlayerModule(playerId, moduleId, registerService);


        if (serverId == null) {
            serverId = redisPlayerInstance.selectPlayerModule(playerId,moduleId,registerService);
            if (serverId == null ){
                ServerVo serverVo = registerService.selectServerInfo(moduleId, playerId);
                if (serverVo != null){
                    localPlayerInstance.registerInfo(playerId,moduleId,serverVo.getServerId());
                    redisPlayerInstance.registerInfo(playerId,moduleId,serverVo.getServerId());
                    serverId = serverVo.getServerId();
                }

            }else {
                localPlayerInstance.registerInfo(playerId,moduleId,serverId);
            }

        }
        return serverId;
    }





}
