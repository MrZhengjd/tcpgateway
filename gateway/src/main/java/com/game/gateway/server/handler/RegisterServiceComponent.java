package com.game.gateway.server.handler;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.game.mj.constant.InfoConstant;
//import com.game.common.model.vo.ServerVo;
import com.game.mj.model.ServerVo;
import com.game.gateway.config.GateWayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
@Service
public class RegisterServiceComponent  {
    private static final int weight = 200;
    @NacosInjected
    private NamingService namingService;
    private Map<Integer,String> moduleNameInfos = new HashMap<>();
    private Map<Integer, List<ServerVo>> serverInfos = new HashMap<>();
    @Autowired
    private GateWayConfig gateWayConfig;
    public ListView findServiceServer(Integer serviceId){
        try {
            return namingService.getServicesOfServer(serviceId,100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public ServerVo selectServerInfo(Integer moduleId,Long playerId){
        Map<Integer, List<ServerVo>> serverInfos = this.serverInfos;
        if (serverInfos == null || serverInfos.isEmpty()){
            return null;
        }
        List<ServerVo> serverVos = serverInfos.get(moduleId);
        if (serverVos == null || serverVos.isEmpty()){
            return null;
        }
        int count = serverVos.size();
        int index = Math.abs(playerId.hashCode()) % count;
        if (index >= count && count > 0){
            index = count -1;
        }
        return serverVos.get(index);
    }
    public String getModuleName(int moduleId){
        return moduleNameInfos.get(moduleId);
    }
    public boolean isServerOnLine(Integer moduleId, Integer serverId){
        Map<Integer, List<ServerVo>> tempData = this.serverInfos;
        List<ServerVo> holdData = tempData.get(serverId);
        if (holdData == null){
            return false;
        }
        return holdData.stream().anyMatch(e->{
            return e.getServerId() == serverId;
        });
    }
    private void refreshEveryModule(Map<Integer,List<ServerVo>> tempMap){
        List<String> modules = gateWayConfig.getModules();

        modules.stream().forEach(e->{
            try {
                namingService.subscribe(e,event->{
                    List<Instance> data = getServiceInfos(e);
                    boolean set = false;
                    Integer moduleId = -99;
                    boolean checkInfo = false;
                    for (Instance instance : data){
                        ServerVo vo = newServerVo(instance);
                        if (!set){
//                            moduleId =vo.getModuleId();
                            moduleId = vo.getModuleId();
                            set = true;
                        }else {
                            if (moduleId != vo.getModuleId()){
                                throw new RuntimeException("setting info error");
                            }
                        }
                        moduleNameInfos.put(moduleId,e);
                        List<ServerVo> serverVoList = tempMap.get(vo.getModuleId());
                        if (serverVoList == null){
                            serverVoList = new ArrayList<>();
                            tempMap.put(vo.getModuleId(),serverVoList);
                        }
                        serverVoList.add(vo);
                    }

                    this.serverInfos.putAll(tempMap);
                });
            } catch (NacosException ex) {
                ex.printStackTrace();
            }

        });
    }
    private void refreshData(){
        Map<Integer,List<ServerVo>> tempMap = new HashMap<>();
        refreshEveryModule(tempMap);

    }
    public void autoRefreshData(){
        refreshData();
    }
    private ServerVo newServerVo(Instance instance){
        String serverId = instance.getMetadata().get(InfoConstant.SERVER_ID);
        String moduleId = instance.getMetadata().get(InfoConstant.MODULE_ID);
        if (StringUtils.isEmpty(moduleId)) {
            throw new IllegalArgumentException(instance.getIp() + "的服务未配置serviceId");
        }

        if (StringUtils.isEmpty(serverId)) {
            throw new IllegalArgumentException(instance.getIp() + "的服务未配置serverId");
        }
        return new ServerVo(Integer.valueOf(moduleId),Integer.valueOf(serverId),instance.getServiceName(),instance.getIp(),instance.getPort(),instance.getInstanceId());

    }
    public List<Instance> getServiceInfos(String serviceId){

        try {
            return namingService.getAllInstances(serviceId);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }


}
