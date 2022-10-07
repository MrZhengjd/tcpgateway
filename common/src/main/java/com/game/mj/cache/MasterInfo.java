package com.game.mj.cache;

import com.game.mj.constant.InfoConstant;
import com.game.mj.model.MasterChangeInfo;
import com.game.mj.model.MasterCopyInfo;
import com.game.mj.model.NodeInfo;
import com.game.mj.model.vo.MasterChangeVo;
import com.game.mj.util.TopicUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class MasterInfo {
    public static Map<String, NodeInfo> nodeMap = new HashMap<>();
    public static Map<String, MasterChangeVo> masterChangeVoMap = new HashMap<>();
    public static Map<String, String> infoMap = new HashMap<>();
    public static Map<String, Long> baseIdMap = new HashMap<>();
    public static Map<String, Integer> serverIdMap = new HashMap<>();

    public static Integer getServerInfo() {
        return serverIdMap.get(InfoConstant.SERVER_ID);
    }

    public static MasterChangeVo getLastMasterChange() {
        return masterChangeVoMap.get(InfoConstant.MASTER);
    }

    public static void clearMasterChange() {
        masterChangeVoMap.clear();
    }

    public static void saveMasterChangeVo(String key, MasterChangeVo masterChangeVo) {
        masterChangeVoMap.put(key, masterChangeVo);
    }

    public static MasterChangeVo getByKey(Integer key) {
        return masterChangeVoMap.get(key);
    }

    public static void saveServerInfo(Integer serverId) {
        serverIdMap.put(InfoConstant.SERVER_ID, serverId);
    }

    public static NodeInfo getMasterInfo() {
        return nodeMap.get(InfoConstant.MASTER);
    }

    public static void changeInfo(String node, Long playerId) {
        infoMap.put(InfoConstant.INFO, node);
        baseIdMap.put(InfoConstant.INFO_ID, playerId);
    }
    public static void saveBaseIdInfo(Long index){
        baseIdMap.put(InfoConstant.INDEX,index);
    }
    public static Long getIndexInfo(){
        return baseIdMap.get(InfoConstant.INDEX);
    }

    public static void bandServer(){
        infoMap.put(InfoConstant.BAND_INFO,"success");
    }
    public static Long getPlayerId() {
        return baseIdMap.get(InfoConstant.INFO_ID);
    }

    public static String getBandInfo(){
        return infoMap.get(InfoConstant.BAND_INFO);
    }
    public static String getInfo() {
        return infoMap.get(InfoConstant.INFO);
    }

    public static void changeMaster(NodeInfo node) {
        nodeMap.put(InfoConstant.MASTER, node);
    }

    public static boolean checkIsMasterOrRecorder() {
        NodeInfo masterInfo = getMasterInfo();
        if (masterInfo == null) {
            return true;
        }
        String recorder = TopicUtil.generateTopic(InfoConstant.LOCAL_HOST, InfoConstant.RECORDER_PORT);
        String s = TopicUtil.generateTopic(masterInfo.getHost(), masterInfo.getPort());
        return recorder.equals(s) || s.equals(FastChannelInfo.getChannelInfo());
    }

    public static boolean checkIsRecorder() {
        return getInfo().equals("recorder");
    }

    private static boolean checkIsSame(MasterChangeInfo masterChangeInfo) {
        return FastChannelInfo.getChannelInfo().equals(masterChangeInfo.getCopyInfo().getNodeInfo());
    }

    /**
     * 提炼
     *
     * @param lastCopyId
     * @param changeInfo
     * @return
     */
    private static MasterCopyInfo getSimpleResult(Long lastCopyId, MasterChangeInfo changeInfo) {
        MasterCopyInfo result = new MasterCopyInfo();
        if (changeInfo != null && changeInfo.getCopyInfo() != null && !checkIsSame(changeInfo)) {
            result.setHostName(changeInfo.getCopyInfo().getNodeInfo());
            result.setStartCopyId(lastCopyId);
            return result;
        }
        return null;

    }

    /**
     * 获得master信息
     *
     * @param lastCopyId
     * @return
     */
    public static MasterCopyInfo needCopy(Long lastCopyId) {
        try {
            MasterChangeVo vo = masterChangeVoMap.get(InfoConstant.MASTER);
            if (vo == null) {
                return null;
            }
            if (vo.getFirst() == null && vo.getSecond() == null) {
                return null;
            }
//            MasterCopyInfo result = new MasterCopyInfo();
            MasterCopyInfo first = null;
            MasterCopyInfo second = null;
            if (vo.getFirst() != null ) {
                first = getSimpleResult(lastCopyId, vo.getFirst());

            }
            if (vo.getSecond() != null ) {
                second = getSimpleResult(lastCopyId, vo.getSecond());
            }
            if (first == null && second == null){
                return null;
            }
            MasterCopyInfo result = new MasterCopyInfo();
            if (first != null && second != null){
                result.setHostName(vo.getFirst().getCopyInfo().getNodeInfo());
                result.setStartCopyId(lastCopyId);
                result.setEndCopyId(vo.getSecond().getCopyInfo().getOperateId());
                return result;
            }
            if (first != null && vo.getLatestOperateId() > lastCopyId){
                first.setEndCopyId(vo.getLatestOperateId());
                return first;
            }
            if (second != null && vo.getLatestOperateId() > lastCopyId){
                second.setEndCopyId(vo.getLatestOperateId());
                return second;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
