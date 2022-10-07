package com.game.diststore.service;

import com.game.mj.cache.MasterInfo;
import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.concurrent.IGameEventExecutorGroup;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.constant.InfoConstant;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventcommand.EventType;
import com.game.mj.eventcommand.IEvent;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.generator.IdGenerator;
import com.game.mj.generator.IdGeneratorFactory;
import com.game.mj.model.*;
import com.game.mj.model.vo.MasterChangeVo;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.mj.store.DefaultIndex;
import com.game.mj.store.Index;
import com.game.mj.util.TopicUtil;
import com.game.network.client.ConnectTools;
import com.game.diststore.model.SelectInfo;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.diststore.util.BackupUtil;
import com.game.diststore.util.HandleUtil;
import com.game.mj.messagedispatch.GameDispatchService;
import com.game.mj.messagedispatch.GameMessageDispatch;
import com.game.network.cache.ChannelMap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zheng
 */
@Component
@GameDispatchService
public class RecordComponent {

    private static Logger logger = LoggerFactory.getLogger(RecordComponent.class);
//    public static final String configPath0 = System.getProperty("user.dir") + File.separator + "diststore" + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    private static ClassPathResource classPathResource = new ClassPathResource("data");
    private IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
    //    private EventExecutor scheduledExecutorService = new DefaultEventExecutor();
    private Map<String, NodeInfo> nodeMap = new HashMap<>();
    @Contended
    private volatile boolean initMasterChange = false;
    private SortedMap<Long, MasterChangeInfo> masterChangeMap = new TreeMap<>();
    private ReadWriteLockOperate lockUtil = new ReadWriteLockOperate();
    private AtomicInteger nodeId = new AtomicInteger(1);
    private Index pageIndex = new DefaultIndex("recorder", classPathResource.getPath() +File.separator+ "recorder", 1);
    //    private UnLockWALQueue masterChangeQueue = UnLockQueueOneInstance.getInstance().getMasterChangeQueue();
    private ThreadLocalRandom random = ThreadLocalRandom.current();

    public Integer getCurrentNode() {
        return nodeId.get();
    }

    public void trySet(Integer oldValue, Integer newValue) {
        nodeId.compareAndSet(oldValue, newValue);
    }

    @Contended
    private volatile NodeInfo master;
    @Contended
    private volatile boolean havedChoosenMaster = false;
    @Contended
    private volatile boolean startLog = false;
    //选举id
    private AtomicInteger selectId = new AtomicInteger(1);
    @Contended
    private volatile boolean changingMaster = false;
    @Contended
    private volatile boolean masterOffLine = false;
    @Contended
    private Long lastOperateId = 1l;
    @Contended
    private Long lastMasterOfflineTime = -1l;
    @Contended
    private Long lastSelectTile = -1l;
    private LockQueueMediator masterChangeMediator = UnLockQueueOneInstance.getInstance().getMasterChangeMediato();
    @Autowired
    private ConnectTools connectTools;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    private BackupUtil backupUtil;
    @Autowired
    private ChannelMap channelMap;

    public Integer getSelectCount() {
        return selectId.get();
    }

    public long getLastWriteId() {
        return pageIndex.getWriteIndex();
    }

    public void rollBack(){
        synchronized (InfoConstant.LOCAL_HOST.intern()){
            if (masterChangeMediator != null){
                masterChangeMediator.rollback();
            }
        }


    }
    /**
     * 当有换新的master 就记录一个信息
     *
     * @param nodeId
     * @param operateId
     * @param mediator
     * @param serverId
     * @return
     */
    public boolean writeData(Integer nodeId, long operateId, LockQueueMediator mediator) {
        if (changingMaster) {
            return false;
        }
        if (nodeId < getCurrentNode()) {
            return false;
        }
        if (operateId < lastOperateId) {
            return false;
        }
        if (master == null) {
            return false;
        }
        pageIndex.setWriteIndex(operateId);
        lastOperateId = operateId;
        master.setLatestOperateId(lastOperateId);
        logger.info("change latest operate id " + lastOperateId);
        if (nodeId != getCurrentNode()) {
//
            IEvent iEvent = getCreateEvent("ok");
            iEvent.setEventId(operateId);
            iEvent.setCalledId(nodeId);
            mediator.execute(iEvent);
        }
        return true;

    }

    public static IEvent getCreateEvent(Object data) {
        IEvent iEvent = new IEvent();

        iEvent.setData(data);
        iEvent.setType(EventType.CREATE.getType());
        return iEvent;
    }

    /**
     * 获得需要索引的事件
     *
     * @param data
     * @return
     */
    public static IEvent getCreateEventWithIndex(Object data) {
        IEvent event = getCreateEvent(data);
        event.setUsedIndex(true);
        return event;
    }

    /**
     * 添加address 到nodeMap
     *
     * @param address
     */
    public void addAddressToNodeMap(InetSocketAddress address) {
        String key = TopicUtil.generateTopic(address.getHostName(), address.getPort());
        if (!nodeMap.containsKey(key)) {
            nodeMap.put(key, new NodeInfo(address.getHostString(), address.getPort()));
        }

    }


    /**
     * 不是主机就直接删除信息
     * 主机给10秒的延迟
     *
     * @param address
     */
    public void removeAddress(InetSocketAddress address) {
        String key = TopicUtil.generateTopic(address.getHostName(), address.getPort());
        NodeInfo node = nodeMap.get(key);
        if (node != null) {
            if (node.isMaster()) {
                lastMasterOfflineTime = System.currentTimeMillis();
                masterOffLine = true;
            } else {
                nodeMap.remove(key);
            }

        }

    }

    public NodeInfo getMaster() {
        if (changingMaster) {
            return null;
        }
        return master;
    }

    public void countDown(String key) {
//
        lockUtil.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                CountDownLatch countDownLatch = backupUtil.getCountDownLatch(key);
                if (countDownLatch == null) {
                    return;
                }
                countDownLatch.countDown();
//
                if (countDownLatch.getCount() == 0){
                    selectMaster(backupUtil.getByKey(key), key);
                }

                logger.info("here is the finish ask copy info");
            }
        });

//        countDownLatch.notifyAll();

    }

    /**
     * 获得参与选主的候选人
     *
     * @param holds
     * @return
     */
    public List<CopyInfo> getWaitSelectDatas(List<CopyInfo> holds) {
        if (holds == null) {
            return null;
        }
        List<CopyInfo> selects = new ArrayList<>();
        for (CopyInfo copyInfo : holds) {
            if (copyInfo.getOperateId() == lastOperateId) {
                selects.add(copyInfo);
            }
        }
        return selects;
    }

    /**
     * 自动选master
     */
    private void autoSelectMaster() {
        if (!havedChoosenMaster || masterOffLine == true) {
            boolean autoSelect = true;
            if (master != null) {
                String key = TopicUtil.generateTopic(master.getHost(), master.getPort());
                if (nodeMap.containsKey(key)) {
                    if (System.currentTimeMillis() - lastMasterOfflineTime > 3000) {
                        nodeMap.remove(key);
                        autoSelect = true;
                    }

                }
            }
            if ((System.currentTimeMillis() - lastSelectTile) > 5000) {
                autoSelect = true;
            }

            if (autoSelect && !nodeMap.isEmpty() && !changingMaster) {
                String tem = TopicUtil.generateTopic(InfoConstant.SELECT, selectId.get());
                CountDownLatch countDownLatch = backupUtil.getCountDownLatch(tem);
                if (countDownLatch != null && countDownLatch.getCount() > 0) {
                    return;
                }
                changingMaster = true;

                int selectCount = selectId.incrementAndGet();
                int size = nodeMap.keySet().size();
                CountDownLatch latch = new CountDownLatch(size);
                SelectInfo selectInfo = new SelectInfo();
                selectInfo.setLatch(latch);

                String key = TopicUtil.generateTopic(InfoConstant.SELECT, selectCount);
                backupUtil.saveLatchInfo(key, latch);
                backupUtil.saveSelectInfo(key, selectInfo);
                long timeMillis = System.currentTimeMillis();
                GameMessage gameMessage = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.ASK_COPY_ID);
//                gameMessage.getHeader().setPlayerId(MasterInfo.getPlayerId());
                gameMessage.setMessageData(key);
                for (Map.Entry<String, NodeInfo> entry : nodeMap.entrySet()) {
                    NodeInfo value = entry.getValue();
                    connectTools.writeMessage(value.getHost(), value.getPort(), gameMessage);
                }
                lastSelectTile = System.currentTimeMillis();
                System.out.println("select master start------------");
                PromiseUtil.keyTimerTask(RecordComponent.class.getSimpleName(), new Runnable() {
                    @Override
                    public void run() {
                        lockUtil.writeLockOperation(new Operation() {
                            @Override
                            public void operate() {
                                CountDownLatch countDownLatch = backupUtil.getCountDownLatch(key);
                                if (countDownLatch == null) {
                                    return;
                                }
                                if (countDownLatch.getCount() > 0){
                                    backupUtil.removeKey(key);
                                    return;
                                }
                                selectMaster(backupUtil.getByKey(key), key);
                            }
                        });

                    }
                },5l,TimeUnit.SECONDS);
//                try {
//                    backupUtil.getCountDownLatch(key).await(5, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    return;
//                }
//                System.out.println("waittime " + (System.currentTimeMillis() - lastSelectTile));
////                List<CopyInfo> waitSelects = getWaitSelectDatas(backupUtil.getByKey(key));

//                backupUtil.removeKey(key);
            }
        }

    }

    /**
     * 随机选出一个master
     *
     * @param selectData
     */
    public void selectMaster(List<CopyInfo> selectData, String key) {
        SelectInfo selectInfo = backupUtil.getSelectInfo(key);
        if (selectInfo == null || selectInfo.getStatus() == 1) {
            changingMaster = false;
            return;
        }

        selectInfo.setStatus(1);
        if (selectData == null || selectData.isEmpty()) {
            changingMaster = false;
            return;
        }
        int keyIndex = random.nextInt(selectData.size()) - 1;
        if (keyIndex < 0) {
            keyIndex = 0;
        }
        CopyInfo nodeInfo = selectData.get(keyIndex);
        if (nodeInfo == null) {
            changingMaster = false;
            return;
        }
        nodeInfo.setOperateId(lastOperateId);
        masterOffLine = false;
        master = nodeMap.get(nodeInfo.getNodeInfo());
        master.setNodeId(nodeId.incrementAndGet());
        MasterChangeInfo masterInfo = new MasterChangeInfo();
        masterInfo.setCopyInfo(nodeInfo);
        masterInfo.setSelectId(selectId.get());
        master.setSelectId(selectId.get());
        IEvent masterEvent = getCreateEvent(masterInfo);
        masterEvent.setEventId(lastOperateId);
        masterChangeMap.put(lastOperateId, masterInfo);
        masterChangeMediator.execute(masterEvent);
        master.setLatestOperateId(lastOperateId);
//                masterChangeQueue.offerData(dataSerialize.serialize(masterInfo));
        changingMaster = false;
        backupUtil.removeKey(key);
        System.out.println("auto select master to " + master + "/n finish time " + System.currentTimeMillis());


    }



    @GameMessageDispatch(value = @HeaderAnno(serviceId = RequestMessageType.ASK_MASTER_CHANGE,messageType = MessageType.RPCREQUEST))
    public void askMasterChange2(DefaultGameMessage askMasterChangeRequest) {
//        masterChangeQueue.pullWithPosition()
        Long lastCopyId =  (Long) askMasterChangeRequest.deserialzeToData(Long.class);
//        askMasterChangeRequest.setLastCopyId(lastCopyId);
        if (masterChangeMap.isEmpty() && !startLog) {
            startLog = true;
            masterChangeMediator.takeFromLastId(lastCopyId).addListener(new GenericFutureListener<Future<? super IEvent>>() {
                @Override
                public void operationComplete(Future<? super IEvent> future) throws Exception {
                    if (future.isSuccess() && future.get() != null) {
                        handleMasterChange(askMasterChangeRequest,lastCopyId);
                    }
                }
            });
        } else if (masterChangeMap.isEmpty()) {

            HandleUtil.handleAndSendToRequestPlayer(askMasterChangeRequest, null, dynamicRegisterGameService, channelMap, false);

        } else {
            handleMasterChange(askMasterChangeRequest,lastOperateId);
        }


    }
    /**
     * 处理masterchangeRequest
     *
     * @param askMasterChangeRequest
     */
    private void handleMasterChange(AskMasterChangeRequest askMasterChangeRequest) {
        MasterChangeInfo front = null;
        if (askMasterChangeRequest.getLastCopyId() > 0){
            front = getLastChangeInfo(masterChangeMap.subMap(0l, askMasterChangeRequest.getLastCopyId()));
        }
        MasterChangeInfo behind = getFistChangeInfo(masterChangeMap, askMasterChangeRequest.getLastCopyId());
        Integer selectId = front == null ? 0 : front.getSelectId();
        MasterChangeVo vo = new MasterChangeVo(selectId, front, behind, pageIndex.getWriteIndex());
        HandleUtil.handleAndSendToRequestPlayer(askMasterChangeRequest, vo, dynamicRegisterGameService, channelMap, true);
    }
    /**
     * 处理masterchangeRequest
     *
     * @param askMasterChangeRequest
     */
    private void handleMasterChange(DefaultGameMessage askMasterChangeRequest,Long lastOperateId) {
        MasterChangeInfo front = getLastChangeInfo(masterChangeMap.subMap(0l, lastOperateId));
        MasterChangeInfo behind = getFistChangeInfo(masterChangeMap, lastOperateId);

        Integer selectId = front == null ? 0 : front.getSelectId();
        MasterChangeVo vo = new MasterChangeVo(selectId, front, behind, pageIndex.getWriteIndex());
        HandleUtil.handleAndSendToRequestPlayer(askMasterChangeRequest, vo, dynamicRegisterGameService, channelMap, true);
    }
    /**
     * 获取尾部数据
     *
     * @param subMap
     * @return
     */
    private MasterChangeInfo getFirstChangeInfo(SortedMap<Long, MasterChangeInfo> subMap) {
        if (subMap == null || subMap.isEmpty()) {
            return null;
        }
        Long lastKey = subMap.firstKey();
        return subMap.get(lastKey);
    }

    /**
     * 获取尾部数据
     *
     * @param subMap
     * @return
     */
    private MasterChangeInfo getFistChangeInfo(SortedMap<Long, MasterChangeInfo> subMap, Long startKey) {
        Long endKey = idGenerator.generateIdFromServerId(MasterInfo.getServerInfo() == null ? 1 : MasterInfo.getServerInfo());
        return getFirstChangeInfo(subMap.subMap(startKey, endKey));
    }

    /**
     * 获取尾部数据
     *
     * @param subMap
     * @return
     */
    public MasterChangeInfo getLastChangeInfo(SortedMap<Long, MasterChangeInfo> subMap) {
        if (subMap == null || subMap.isEmpty()) {
            return null;
        }
        Long lastKey = subMap.lastKey();
        return subMap.get(lastKey);
    }

    /**
     * 初始化定时器
     */
    public void init() {

        IGameEventExecutorGroup.getInstance().selectByHash(RecordComponent.class.getSimpleName()).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                lockUtil.writeLockOperation(new Operation() {
                    @Override
                    public void operate() {
                        autoSelectMaster();
                    }
                });


            }
        }, 110l, 3000l, TimeUnit.MILLISECONDS);
        System.out.println("start select master");
    }
}
