package com.game.mj.store;

import io.netty.buffer.ByteBuf;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.io.Serializable;

/**
 * @author zheng
 */
public class TempResult implements Serializable,Cloneable {
    private static final String TEMP_RESULT= "temp_result";
    private int state;
    private byte[] datas;
    private int readerPosition;
    private ByteBuf byteBuf;
    private static volatile boolean set0 = false;
    private static volatile boolean set1 = false;
    private static TempResult failedWith0;
    private static TempResult failedWith1;
    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }
    private static class Holder{
        private static TempResult INSTANCE = new TempResult();
    }
    private static class Holder1 {
        private static TempResult INSTANCE = new TempResult();
    }
    private static class Holder2 {
        private static TempResult INSTANCE2 = new TempResult();
    }
    public static TempResult getInstance(){
        return Holder.INSTANCE;
//        try {
//            return (TempResult) Holder.INSTANCE.clone();
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        return Holder.INSTANCE;
    }
    public static TempResult getFailedWith0(){
        if (failedWith0 == null){
            synchronized (TEMP_RESULT.intern()){
                if (failedWith0 == null && !set0){
                    failedWith0 = copyToFail0();
                    set0 = true;
                }
                return failedWith0;
            }
        }
        return failedWith0;

    }

    public static TempResult getFailedWith1(){
        if (failedWith1 == null){
            synchronized (TEMP_RESULT.intern()){
                if (failedWith1 == null && !set1){
                    failedWith1 = copyToFail1();
                    set1 = true;
                }
                return failedWith1;
            }
        }
        return failedWith1;

    }

    public static TempResult copyToFail0(){
        TempResult tempResult = Holder1.INSTANCE;
        tempResult.setState(0);
        return tempResult;
    }

    public static TempResult copyToFail1(){
        TempResult tempResult = Holder2.INSTANCE2;
        tempResult.setState(1);
        return tempResult;
    }
    public static TempResult copyInfo(TempResult playerBaseInfo){
        try {
            MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            MapperFacade mapperFacade = mapperFactory.getMapperFacade();
            TempResult result = mapperFacade.map(playerBaseInfo, TempResult.class);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public TempResult() {
        super();
    }

    public void init(){
        state = 0;
        datas = null;
        readerPosition = 0;
    }
    public int getReaderPosition() {
        return readerPosition;
    }

    public void setReaderPosition(int readerPosition) {
        this.readerPosition = readerPosition;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public byte[] getDatas() {
        return datas;
    }



}
