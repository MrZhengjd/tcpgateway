package com.game.waitstart.nameserver;

import com.game.domain.registerservice.RegisterService;
import com.game.waitstart.config.WaitStartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zheng
 */
@Component
public class RegisterGameService {
    @Autowired
    private RegisterService registerService;
    @Resource
    private WaitStartConfig waitStartConfig;
    public void registerService(){
//        Instance instance = new Instance();
////        instance.setInstanceId(String.valueOf(waitStartConfig.getServerId()));
//        instance.setPort(waitStartConfig.getPort());
//        instance.setServiceName(waitStartConfig.getName());
//        instance.setClusterName(waitStartConfig.getClusterName());
//        instance.setEphemeral(true);
//        instance.getMetadata().put(InfoConstant.SERVER_ID,String.valueOf(waitStartConfig.getServerId()));
//        instance.getMetadata().put(InfoConstant.MODULE_ID,String.valueOf(waitStartConfig.getModuleId()));
//
//        instance.setIp(waitStartConfig.getHost());
//        try {
//            namingService.registerInstance(waitStartConfig.getClusterName(),instance);
//        } catch (NacosException e) {
//            e.printStackTrace();
//        }
        registerService.registerServiceModule(waitStartConfig.getHost(),waitStartConfig.getPort(),waitStartConfig.getClusterName(),
                waitStartConfig.getName(),waitStartConfig.getServerId(),waitStartConfig.getModuleId());
    }
}
