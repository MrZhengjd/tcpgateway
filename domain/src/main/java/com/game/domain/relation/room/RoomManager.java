package com.game.domain.relation.room;

import com.game.mj.concurrent.GameEventExecutorGroup;
import com.game.mj.concurrent.IGameEventExecutorGroup;


//import com.game.domain.model.PlayerRequest;
import com.game.domain.relation.operate.SaveRoomData;
import com.game.domain.relation.player.PlayerBaseInfo;
import com.game.domain.relation.role.PlayerRole;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
public class RoomManager {
    private Room room;
//    private JsonRedisManager jsonRedisManager;
    //定时刷新数据到缓存
    private ScheduledFuture<?> scheduledFuture;
//    private volatile boolean startSaveRoom = false;
    private ScheduledFuture<?> buhuaSchedule;
    private EventExecutor executor;
    private SaveRoomData saveRoomData ;
    public Long getPlayingIndex(){
        return room.getPlayingIndex();
    }
    public RoomManager(Room room) {
        this.room = room;
//        this.jsonRedisManager = jsonRedisManager;
        executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());
    }

//    public RoomManager(Room room) {
//        this.room = room;
//        executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());
//
//    }
    public RoomManager(Room room, SaveRoomData saveRoomData) {
        this.room = room;
        this.saveRoomData = saveRoomData;
        executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());

    }
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

//    public void addPlayer(Player player){
//       addPlayerInfo(player.getBaseInfo());
//       addRole(player.getPlayerRole());
//    }
    public void addPlayerInfo(PlayerBaseInfo playerBaseInfo){
        room.getBaseInfoMap().put(playerBaseInfo.getPlayerId(),playerBaseInfo);
    }
    public void addRole(PlayerRole role){
        room.setPlayerNumber(room.getPlayerNumber()+1);
        role.setPlayerIndex(room.getPlayerNumber());
        room.playerJoinRoom(role);

//        room.addObserver(role);
    }

    public void saveToRedis(){
//        jsonRedisManager.setObjectHash1(InfoConstant.GAME_ROOM,String.valueOf(room.getRoomNum()),room);
        saveRoomData.saveRoomData(room);
    }
    public PlayerRole getById(Long id){
        return  room.getById(id);
    }

//    public RoleManager getOrCreateRoleManager(Long id){
//        RoleManager roleManager;
//        if (roleManagers.containsKey(id)){
//            roleManager = roleManagers.get(id);
//        }else {
//            roleManager = new RoleManager(getById(id));
//        }
//        return roleManager;
//    }


    public void handleTimerTask(Runnable runnable){
//        initExecutor();
//        safeExecute(executor,runnable);
    }
    public void initExecutor(){
        if (executor == null){
            executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());
        }

    }
    public void scheduleSaveData(){
        initExecutor();
        if (!room.getStartSaveRoom()){
            room.setScheduledFuture(executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
//                    jsonRedisManager.setObjectHash1(Constants.ROOM_MAP,String.valueOf(room.getRoomNum()),room);
                    room.setStartSaveRoom(true);
                    if (room.getGameStatus() == 3){
                        room.getScheduledFuture().cancel(true);
                    }
                }
            },20000,100l,TimeUnit.MILLISECONDS));;
        }

    }

    public ScheduledFuture<?> tuoGuan(Runnable runnable, Long init, Long delay, TimeUnit timeUnit){
//        ScheduledFuture<?> schedule =
//
        initExecutor();
//
        return this.executor.schedule(runnable, delay, timeUnit);

    }
//    public Response handleRequest(Long playerId, Integer serviceId, TMessageVo tMessageVo){
//        PlayerRequest playerRequest = new PlayerRequest();
//        playerRequest.setRoom(room);
//        playerRequest.setRequestType(serviceId);
//
//        playerRequest.setPlayerRole(this.getById(playerId));
//        handleRequest(playerRequest);
//        Response response = FastContextHolder.getRuntimeContext().getResponse();
//        FastContextHolder.clear();
//        return response;
//    }

    /**
     * 开始定时托管
     * @param runnable
     * @param init
     * @param delay
     * @param timeUnit
     */
    public void startTuoGuanRotate(Runnable runnable, Long init, Long delay, TimeUnit timeUnit){
        if (!room.getStartTuoGuan()){
            room.setStartTuoGuan(true);
            tuoGuanRotate(runnable,init,delay,timeUnit);
        }
    }
    public void tuoGuanRotate(Runnable runnable, Long init, Long delay, TimeUnit timeUnit){
        ScheduledFuture<Void> scheduledFuture1 = (ScheduledFuture<Void>) tuoGuan(runnable, init, delay, timeUnit);
        scheduledFuture1.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
//                scheduledFuture1.cancel(true);
                if (future.isDone()){
                    scheduledFuture1.cancel(true);
//                scheduledFuture1.cancel(true);
                    if (room.getGameStatus()!= 3 ){
                        tuoGuanRotate(runnable,init,delay,timeUnit);
                    }
                    future.cancel(true);
                }

            }
        });


    }
    /**
     * 处理进来的请求
     * @param request
     */
//    public void handleRequest(final PlayerRequest request){
////        if (executor == null){
////            executor = GameEventExecutorGroup.getInstance().select(room.getRoomNum());
////        }
////        initExecutor();
////        safeExecute(GameEventExecutorGroup.getInstance().select(room.getRoomNum()), new Runnable() {
////                    @Override
////                    public void run() {
////
////
////                    }
////                });
////        CoreEngine engine = new CoreEngine(request);
////        engine.process();
////        if (jsonRedisManager != null){
////            jsonRedisManager.setObjectHash1(Constants.ROOM_MAP,String.valueOf(room.getRoomNum()),room);
////        }
//
//    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

//    public void tuoGuan(Runnable runnable, Long init, Long delay, TimeUnit timeUnit){
////        scheduledFuture = executor.scheduleAtFixedRate(runnable,init,delay,timeUnit);
//
//    }
    public ScheduledFuture<?> scheduledFuture(Runnable runnable, Long init, Long delay, TimeUnit timeUnit,Integer roomNumber){
//        executor.scheduleWithFixedDelay()

//        return GameEventExecutorGroup.getInstance().select(roomNumber).scheduleAtFixedRate(runnable,init,delay,timeUnit);
        return IGameEventExecutorGroup.getInstance().selectByHash(roomNumber).scheduleAtFixedRate(runnable,init,delay,timeUnit);

    }
//    public void tuoGuan(){
//        scheduledFuture = executor.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                if (room.getGameStatus() == 3){
//                    scheduledFuture.cancel(true);
//                }
//            }
//        },10,1000, TimeUnit.MILLISECONDS);
//    }

//    public void tuoGuanBuHua(BuHuaOperation buHuaOperation){
//        buhuaSchedule = executor.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                buHuaOperation.buhua(room);
//                buhuaSchedule.cancel(true);
//            }
//        }, 0, 100, TimeUnit.MILLISECONDS);
//    }
    public interface BuHuaOperation{
        void buhua(Room room);
    }


    public void initPlayingIndex(){
//        room.getRoomInfos().put(Constant.PLAYING_INDEX,1);
        room.setPlayingIndex(1l);
    }
    public void changePlayingIndex(){
//        Integer o = (Integer) room.getRoomInfos().get(Constant.PLAYING_INDEX);
//        changeNameValuePair(Constant.PLAYING_INDEX,"2");
        room.setPlayingIndex(2l);
    }

}
