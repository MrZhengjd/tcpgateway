package com.game.diststore.util;

import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.cache.ReturnOperate;
import com.game.mj.model.CopyInfo;
import com.game.diststore.model.SelectInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author zheng
 */
@Component
public class BackupUtil {

    private Map<String , CountDownLatch> countDownLatchMap = new ConcurrentHashMap<>();
    private Map<String , SelectInfo> selectInfoMap = new HashMap<>();
    private Map<String , List<CopyInfo>> nodeInfoMap = new HashMap<>();
    private ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public CountDownLatch getCountDownLatch(String key){
        return countDownLatchMap.get(key);
    }
    public void removeKey(String key){
        countDownLatchMap.remove(key);
    }
    public SelectInfo getSelectInfo(String key){
        return readWriteLockOperate.writeLockReturnOperation(new ReturnOperate<SelectInfo>() {
            @Override
            public SelectInfo operate() {
                return selectInfoMap.get(key);
            }
        });

    }
    public void removeSelectKey(String key){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                selectInfoMap.remove(key);
            }
        });

    }
    public  void saveSelectInfo(String key,SelectInfo selectInfo){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                selectInfoMap.put(key,selectInfo);
            }
        });

    }
    public  void saveLatchInfo(String key,CountDownLatch countDownLatch){
        countDownLatchMap.put(key,countDownLatch);
    }
    public List<CopyInfo> getByKey(String key){
        return readWriteLockOperate.readLockReturnOperation(new ReturnOperate<List<CopyInfo>>() {
            @Override
            public List<CopyInfo> operate() {
                return nodeInfoMap.get(key);
            }
        });
    }

    public void saveNodeInfo(String  key,CopyInfo nodeInfo){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                List<CopyInfo> data = nodeInfoMap.get(key);
                if (data == null){
                    data = new ArrayList<>();
                }
                data.add(nodeInfo);
                nodeInfoMap.put(key,data);
//                System.out.println("wh");
            }
        });
    }

    /**
     * 检测最新id
     * @param latestOperateId
     */
    public void checkMeetOperate(Long latestOperateId){

    }
}
