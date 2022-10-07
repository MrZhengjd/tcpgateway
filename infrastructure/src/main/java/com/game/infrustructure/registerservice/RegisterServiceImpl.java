package com.game.infrustructure.registerservice;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.client.naming.beat.BeatInfo;
import com.game.mj.constant.InfoConstant;
import com.game.mj.model.ServerVo;
import com.game.domain.registerservice.RegisterService;
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
public class RegisterServiceImpl implements RegisterService {
    private static final int weight = 200;
    @NacosInjected
    private NamingService namingService;

    private Map<Integer,String> moduleNameInfos = new HashMap<>();
    private Map<Integer, List<ServerVo>> serverInfos = new HashMap<>();

    public ListView findServiceServer(Integer serviceId){
        try {
            return namingService.getServicesOfServer(serviceId,100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getModuleName(int moduleId){
        return moduleNameInfos.get(moduleId);
    }

    private void refreshEveryModule(Map<Integer,List<ServerVo>> tempMap,List<String> modules){
//        List<String> modules = gateWayConfig.getModules();

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
    @Override
    public void refreshData(List<String> modules){
        Map<Integer,List<ServerVo>> tempMap = new HashMap<>();
        refreshEveryModule(tempMap,modules);

    }

    @Override
    public String getModuleName(Integer moduleId) {
        return moduleNameInfos.get(moduleId);
    }

    public void autoRefreshData(List<String> modules){
        refreshData(modules);
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
    @Override
    public boolean checkServerOnLine(Integer moduleId, Integer serverId) {
        Map<Integer, List<ServerVo>> tempData = this.serverInfos;
        List<ServerVo> holdData = tempData.get(serverId);
        if (holdData == null){
            return false;
        }
        return holdData.stream().anyMatch(e->{
            return e.getServerId() == serverId;
        });
    }

    @Override
    public ServerVo selectServerInfo(Integer moduleId, Long playerId) {
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

    @Override
    public void registerServiceModule(String host, int port, String clusterName, String serviceName, Integer serviceId, Integer serverId) {
        try {
            BeatInfo beatInfo = new BeatInfo();
            Instance instance = new Instance();
            instance.setIp(host);
            instance.setPort(port);
            instance.setClusterName(clusterName);
            instance.setServiceName(serviceName);
            instance.setEphemeral(true);
            instance.getMetadata().put(InfoConstant.MODULE_ID,String.valueOf(serviceId));
            instance.getMetadata().put(InfoConstant.SERVER_ID,String.valueOf(serverId));
            namingService.registerInstance(clusterName,instance);


        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
