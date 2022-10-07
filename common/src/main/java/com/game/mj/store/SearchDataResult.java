package com.game.mj.store;

import io.netty.buffer.ByteBuf;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.io.Serializable;

/**
 * @author zheng
 */
public class SearchDataResult implements Serializable,Cloneable {
    private static final String TEMP_RESULT= "temp_result";
    private int state;
    private byte[] datas;
    private int readerPosition;
    private BlockInfo blockInfo;
    private ByteBuf byteBuf;
    private static volatile boolean set0 = false;
    private static volatile boolean set1 = false;
    private static SearchDataResult failedWith0;
    private static SearchDataResult failedWith1;
    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public BlockInfo getBlockInfo() {
        return blockInfo;
    }

    public void setBlockInfo(BlockInfo blockInfo) {
        this.blockInfo = blockInfo;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }
    private static class Holder{
        private static SearchDataResult INSTANCE = new SearchDataResult();
    }
    private static class Holder1 {
        private static SearchDataResult INSTANCE = new SearchDataResult();
    }
    private static class Holder2 {
        private static SearchDataResult INSTANCE2 = new SearchDataResult();
    }
    public static SearchDataResult getInstance(){
        return Holder.INSTANCE;
//        try {
//            return (TempResult) Holder.INSTANCE.clone();
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        return Holder.INSTANCE;
    }
    public static SearchDataResult getFailedWith0(){
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

    public static SearchDataResult getFailedWith1(){
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

    public static SearchDataResult copyToFail0(){
        SearchDataResult tempResult = Holder1.INSTANCE;
        tempResult.setState(0);
        return tempResult;
    }

    public static SearchDataResult copyToFail1(){
        SearchDataResult tempResult = Holder2.INSTANCE2;
        tempResult.setState(1);
        return tempResult;
    }
    public static SearchDataResult copyInfo(SearchDataResult playerBaseInfo){
        try {
            MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            MapperFacade mapperFacade = mapperFactory.getMapperFacade();
            SearchDataResult result = mapperFacade.map(playerBaseInfo, SearchDataResult.class);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public SearchDataResult() {
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
