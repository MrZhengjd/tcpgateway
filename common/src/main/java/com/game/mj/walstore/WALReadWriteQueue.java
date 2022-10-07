package com.game.mj.walstore;


//import com.game.common.model.msg.Message;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.mj.store.*;
import io.netty.buffer.PooledByteBufAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Contended;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import static com.game.mj.store.UnLockQueueInstance.BLOCK_FILE_SUFFIX;


/**
 * @author zheng
 * 不循环的wah queue
 */

public class WALReadWriteQueue {
    @Contended("readWriteSame")
    private volatile boolean readWriteSame;

    @Contended("readPosition")
    private volatile int readPosition;

    @Contended("writePosition")
    private volatile int writePosition;

    @Contended
    private volatile long readIndex;
    @Contended
    private volatile long writeIndex;
    @Contended("currentWritePage")
    private volatile int currentWritePage;


    @Contended("currentReadPage")
    private volatile int currentReadPage;

    @Contended
    private volatile long comming= 0l;

    @Contended("rotateWrite")
    private volatile boolean rotateWrite;
    @Contended("writeSync")
    private volatile boolean writeSync;
    @Contended("readSync")
    private volatile boolean readSync;
    private volatile boolean rotateRead;
    private Map<Integer,Integer> havedReadPageMap = new HashMap<>();
    @Contended
    private volatile long readingCount = 0l;
    private DataSerialize serialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
//    private volatile long nutNullCount = 0l;
//
//    public long getNutNullCount() {
//        return nutNullCount;
//    }

    @Contended
    private volatile SoftReference<TempResult> softReference = new SoftReference<TempResult>(new TempResult());
    public static final int MAX_BLOCK = 128;
    public static final int BLOCK_SIZE =  128 * 1024 * 1024;//32MB

    public static final int THREAD_PARK_NANOS = 30;
    private AtomicBoolean writeLock = new AtomicBoolean(false);
    private AtomicBoolean common = new AtomicBoolean(false);
    private AtomicBoolean differ = new AtomicBoolean(false);
    private AtomicLong empty = new AtomicLong(0);
    public Long getEmpty(){
        return empty.get();
    }
    public PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    private String name;
    private String dir;
    private QueueBlock writeBlock;
    private QueueBlock[] blocks;
    private PageIndex[] pageIndices;
    private PageIndex readPage;
    private PageIndex writePage;
    private QueueBlock readBlock;
    private static Map<Integer,PullData> pullDataMap = new HashMap<>();
    private PullData pullData;
//    private static ByteBuf byteBuf;
//    private final TempResult tempResult = TempResult.getInstance();
    private static Logger logger = LoggerFactory.getLogger(WALReadWriteQueue.class);
    private Index readwriterPageIndex;

    public TempResult getBySoft(){
        TempResult tempResult = softReference.get();
        if (tempResult == null){
            tempResult = new TempResult();
            softReference = new SoftReference<>(tempResult);
        }
//        tempResult.init();
        return tempResult;
    }

    public int getCurrentWritePage() {
        return currentWritePage;
    }

    public int getCurrentReadPage() {
        return currentReadPage;
    }

    public Long getComming() {
        return comming;
    }
//    public boolean write(Message message) {
//        return offerData1(readyData(message))>0;
//    }
    public WALReadWriteQueue(String name, String dir) {
        this.name = name;
        this.dir = dir;
        this.readwriterPageIndex = new DefaultIndex(name, dir, 0);
        pageIndices = new DefaultPageIndex[MAX_BLOCK];
//        for (int i = 0;i<MAX_BLOCK;i++){
//            pageIndices[i] = new DefaultPageIndex(name, dir, i );
//        }
        currentWritePage = 0;
        currentReadPage = 0;
        readWriteSame=true;
        writePage = new DefaultPageIndex(name,dir,currentWritePage);
        readPage = new DefaultPageIndex(name,dir,currentReadPage);
        readPosition = readPage.getReadPosition();
        writePosition = writePage.getWriterPosition();
        readIndex = readwriterPageIndex.getReadIndex();
        writeIndex = readwriterPageIndex.getWriteIndex();
        String filePath = formatBlockFilePath(name, 0, dir);
        writeBlock = new QueueBlock(writePage, readPage, filePath);
        readBlock = new QueueBlock(writePage, readPage, filePath);
        blocks = new QueueBlock[MAX_BLOCK];
        blocks[writePage.getWriterPosition() % MAX_BLOCK] = writeBlock;
//        readBlock.setByteBuf(allocator.directBuffer());

        if (!pullDataMap.containsKey(2)){
            pullDataMap.put(2,new CachePullData());
        }
        pullData = pullDataMap.get(2);

    }
    public static String formatBlockFilePath(String queueName, int fileNum, String fileBackupDir) {
        return fileBackupDir + File.separator + String.format("tblock_%s_%d%s", queueName, fileNum, BLOCK_FILE_SUFFIX);
    }
    private void unLock(AtomicBoolean lock) {
        lock.set(false);
    }
    private void lockRead(AtomicBoolean lock) {
//        long start = System.currentTimeMillis();
        while (true) {
            if (rotateRead ){
                LockSupport.parkNanos(WALReadWriteQueue.THREAD_PARK_NANOS);
                continue;
            }
            if (lock.compareAndSet(false, true)) {
//                System.out.println("cat lock "+(System.currentTimeMillis() - start));
                return;
            }
            LockSupport.parkNanos(WALReadWriteQueue.THREAD_PARK_NANOS);
        }

    }
    private void lockWrite(AtomicBoolean lock) {
//        long start = System.currentTimeMillis();
        while (true) {
            if (rotateWrite ){
                LockSupport.parkNanos(WALReadWriteQueue.THREAD_PARK_NANOS);
                continue;
            }
            if (lock.compareAndSet(false, true)) {
//                System.out.println("cat lock "+(System.currentTimeMillis() - start));
                return;
            }
            LockSupport.parkNanos(WALReadWriteQueue.THREAD_PARK_NANOS);
        }

    }
    private void lock(AtomicBoolean lock) {
//        long start = System.currentTimeMillis();
        while (true) {
            if (lock.compareAndSet(false, true)) {
//                System.out.println("cat lock "+(System.currentTimeMillis() - start));
                return;
            }
            LockSupport.parkNanos(WALReadWriteQueue.THREAD_PARK_NANOS);
        }

    }


    /**
     * 是否需要翻到下一页写数据
     *
     * @param readPosition
     * @param writePosition
     * @param length
     * @return
     */
    private boolean needRotateWrite(int readPosition, int writePosition, int length) {

        int increment = length + 4;
        if ( writePosition <= BLOCK_SIZE) {

            int temp =  BLOCK_SIZE - writePosition;
//            if (writePosition == 999){
//                System.out.println("here is comming "+temp + " and increment "+(increment+4));
//            }
            return temp < increment + 4;
        }
        return false;
    }

    /**
     * 获取操作空间信息
     *
     * @param readPosition
     * @param writePosition
     * @param length
     * @param pageIndex
     * @return
     */
    public BlockInfo calWriteInfo(int readPosition, int writePosition, int length, int pageIndex) {
        boolean needRotate = needRotateWrite(readPosition, writePosition, length);
//        boolean needRotate = readBlock.checkNeedWriteRotate(readPosition,writePosition,length);
        BlockInfo blockInfo = new BlockInfo(writePosition, readPosition, pageIndex);
        if (needRotate) {
//            System.out.println("rotate read write position "+readPosition +" werite "+writePosition+ " length "+length +" at page index "+pageIndex);
//            pageIndex ++;
            blockInfo.setPageIndex(pageIndex + 1);
            if (pageIndex + 1 - currentReadPage > MAX_BLOCK) {
                blockInfo.setUnReach(true);
                return blockInfo;
            }
            blockInfo.setStartwritePosition(0);
            blockInfo.setAfterWriteIndex(length + 4);
            return blockInfo;
        }
        int increment = length + 4;

        int totalLength = writePosition + increment;
        if (totalLength > BLOCK_SIZE) {
//            logger.info("bigger than size "+writePosition +" and increment "+increment);
            blockInfo.setAfterWriteIndex(increment);
            blockInfo.setStartwritePosition(0);
            return blockInfo;
//            logger.info("bigger than size "+writePosition +" and increment "+increment+ " after write index "+blockInfo.getAfterWriteIndex());

        } else {
            blockInfo.setStartwritePosition(writePosition);
            blockInfo.setAfterWriteIndex(writePosition + increment);
        }
        return blockInfo;

    }


    /**
     *
     */
    private boolean lockCommon(boolean write){
//        long start = System.currentTimeMillis();

        while (readWriteSame){
            if (rotateRead ){
                LockSupport.parkNanos(THREAD_PARK_NANOS);
                continue;
            }
            if(common.compareAndSet(false, true)){
                return true;
            }
            if (this.currentReadPage != this.currentWritePage && !write){
                return false;
            }
            LockSupport.parkNanos(THREAD_PARK_NANOS);
        }
        return false;

    }
    private void unLockCommon(boolean lockCommon){
        if (lockCommon){
            unLock(common);
//            System.out.println("unlock page "+pageIndex);
        }
    }
    public long getReadingCount(){
        return readingCount;
    }


    private TempResult pullSuccess1() {
//        boolean b = lockCommon();
        lockRead(differ);
        boolean b = lockCommon(false);



//        result.init();



        try {
            boolean needSet = false;
            int pageIndex = currentReadPage;
            if (readBlock == null || readPage == null) {
                if (readPage == null) {
                    readPage = new DefaultPageIndex(name, dir, pageIndex );
                }
                if (readBlock == null) {
                    readBlock = new QueueBlock(writePage, readPage, formatBlockFilePath(name, pageIndex % MAX_BLOCK, dir));
                    readBlock.setPageIndex(pageIndex);
                }

            }
            if (readIndex > writeIndex){
                TempResult result = getBySoft();
                result.setState(0);
//                logger.info("differ page "+currentReadPage +" old page "+pageIndex);
                return result;
            }
            if (currentReadPage != pageIndex || havedReadPageMap.containsKey(pageIndex)) {
                TempResult result = getBySoft();
                result.setState(1);
//                logger.info("differ page "+currentReadPage +" old page "+pageIndex);
                return result;
            }
//

            readingCount++;
//            nutNullCount ++;
            int tempWrite;
            if (readWriteSame){
                tempWrite = this.writePosition;
            }else {
                tempWrite = readPage.getWriterPosition();
            }
//            int tempRead = this.readPosition;
//

            BlockInfo readInfo = readBlock.getNextReadInfo2(this.readPosition, tempWrite, this.currentReadPage);
//
            if (readInfo == null || readInfo.getPageIndex() > this.currentWritePage || readInfo.isUnReach() || readInfo.getLength() <=0) {
                TempResult result = getBySoft();
                result.setState(0);
//                logger.info("out of reach----------------");
                return result;
            }

            if (readInfo.getPageIndex() != pageIndex){
                rotateRead = true;
//                long timeMillis = System.currentTimeMillis();
//
                havedReadPageMap.put(pageIndex,1);
                if ( readInfo.getPageIndex() == this.currentWritePage){
                    readWriteSame = true;
                }
                this.currentReadPage = readInfo.getPageIndex();
//                pageIndices[currentReadPage % MAX_BLOCK]=readPage;
                if (pageIndices[readInfo.getPageIndex() % MAX_BLOCK] != null){
                    readPage = pageIndices[readInfo.getPageIndex() % MAX_BLOCK];
                }else {
                    readPage =new DefaultPageIndex(name,dir,readInfo.getPageIndex());
                }

                if (readInfo.getPageIndex() != currentWritePage){
                    readBlock = blocks[readInfo.getPageIndex() % MAX_BLOCK];
                }else {
                    readBlock = new QueueBlock(writePage, readPage, formatBlockFilePath(name, readInfo.getPageIndex() % MAX_BLOCK, dir));
                }
                readBlock.setPageIndex(readInfo.getPageIndex());

                this.readPosition = readPage.getReadPosition();
//                this.readwriterPageIndex.setReadPosition(this.readPosition);
                TempResult result = getBySoft();
                result.setState(1);
                rotateRead = false;
//                logger.info("rotate time -------"+(System.currentTimeMillis() - timeMillis));
                return result;
            }

            TempResult tempResult = TempResult.getInstance();
//            tempResult.init();
//            boolean lockCommon = lockCommon();
            readBlock.read23(readInfo.getStartReadPositon(), tempWrite, readInfo.getLength(),tempResult);
//            unLockCommon(lockCommon);
//            tempResult.setReaderPosition();
            this.readPosition = readInfo.getAfterReaderIndex();
            tempResult.setState(2);

            if (readWriteSame){
                this.writePage.setReadPosition(this.readPosition);
            }
//            this.readIndex ++;
            readwriterPageIndex.setReadIndex(this.readIndex++);

            return tempResult;
        } finally {
            unLockCommon(b);
            unLock(differ);

//
        }
    }



//    public byte[] readyData(Message message) {
//        ByteBuf byteBuf = allocator.directBuffer();
//        message.writeToByteBuf(byteBuf,serialize);
//        int length = byteBuf.readableBytes();
////        System.out.println("out readable bytes "+byteBuf.readableBytes());
//        if (length > 0) {
////            System.out.println("here is coming ======================");
//            byte[] datas = new byte[length];
//            byteBuf.readBytes(datas);
//            byteBuf.release();
//            return datas;
//        }
//        byteBuf.release();
//        return null;
//    }

//    public boolean write(byte[] data) {
//        return offer(data);
//    }

//    public boolean write(Message message) {
//        return offer(readyData(message));
//    }





    public int offerData1(byte[] bytes) {
//        long start = System.currentTimeMillis();
        lock(writeLock);
        boolean b = lockCommon(true);



        try {
            int writePageIndex = this.currentWritePage;
            if (writeBlock == null || writePage == null) {
                if (writePage == null) {
                    writePage = new DefaultPageIndex(name, dir, writePageIndex );
                }
                if (writeBlock == null) {
                    writeBlock = new QueueBlock(writePage, readPage, formatBlockFilePath(name, writePageIndex % MAX_BLOCK, dir));
                    writeBlock.setPageIndex(writePageIndex);
                }

            }
            comming ++;
//            Long current = System.currentTimeMillis();
//            System.out.println("here is finish "+(current - start));


            if (currentWritePage != writePageIndex){
                return 0;
            }

            int tempReadPosition = 0;
            if (readWriteSame || this.currentReadPage == this.currentWritePage){
                tempReadPosition = this.readPosition;
            }else {
                tempReadPosition = writePage.getReadPosition();
//                logger.info("use write page ================");
            }

//            long start = System.currentTimeMillis();
            BlockInfo info = calWriteInfo(tempReadPosition, this.writePosition, bytes.length, currentWritePage);
//            long start = System.currentTimeMillis();
            if (info.getPageIndex() != currentWritePage) {
                if (info.isUnReach()) {
                    return -1;
                }
//                logger.info("rotate ------"+tempReadPosition +" write position "+ this.writePosition);
                rotateWrite = true;
                readWriteSame = false;
                blocks[currentWritePage % MAX_BLOCK] = writeBlock;
//                pageIndices[info.getPageIndex()-1 % MAX_BLOCK] = writePage;
//                writePage = new DefaultPageIndex(name, dir, info.getPageIndex() );
                pageIndices[currentWritePage % MAX_BLOCK] = writePage;
//                logger.info("save time "+(System.currentTimeMillis() - start));


                if (pageIndices[info.getPageIndex() % MAX_BLOCK] != null){
                    writePage = pageIndices[info.getPageIndex() % MAX_BLOCK];
                }else {
                    writePage = new DefaultPageIndex(name,dir,info.getPageIndex());
                }
//                this.writePage.setReadPosition(0);
                writeBlock = new QueueBlock(writePage, readPage, formatBlockFilePath(name, info.getPageIndex() % MAX_BLOCK, dir),info.getPageIndex());;
                this.currentWritePage = info.getPageIndex();
                this.writePosition = 0;

                rotateWrite = false;
//                logger.info("rotate tiem "+(System.currentTimeMillis() - start) + " current read "+currentReadPage +" current write "+currentWritePage+ " read positin "+readPosition + " write position "+writePosition);
                return 0;
//
            }

            writeBlock.writeData2(bytes);
            this.writePosition = info.getAfterWriteIndex();
//            this.writePage.setWriterPosition(info.getAfterWriteIndex());
//            this.readwriterPageIndex.setWriterPosition(this.writePosition);
            if (readWriteSame ) {
                this.readPage.setWriterPosition(this.writePosition);
            }
            readwriterPageIndex.setWriteIndex(this.writeIndex++);
//            System.out.println("here is finish "+(System.currentTimeMillis() - start));
//            coming.getAndIncrement();
            return 1;
        } finally {
            unLockCommon(b);
            unLock(writeLock);
        }

    }
//
    private interface PullData{
        TempResult pullData();
    }

    private class CachePullData implements PullData{

        @Override
        public TempResult pullData() {
            return pullSuccess1();
        }
    }

    public TempResult poll2(){

        TempResult result = pullData.pullData();
//        if (result.getState() == 0) {
//            return null;
//        } else if (result.getState() == 1) {
//            return poll2();
//
//        } else {
//
//            return result;
//        }
        return result;
    }



    /**
     * 检测数据是否正常
     *
     * @param result
     * @return
     */
    private byte[] checkResult(TempResult result) {
        if (result.getState() == 0) {
            return null;
        } else if (result.getState() == 1) {
            TempResult tempResult = pullSuccess1();
            return checkResult(tempResult);

        } else {
            if (result.getReaderPosition() == 33554412){
                logger.info("data ");
            }
            return result.getDatas();
        }
    }

//    public Integer getComming() {
//        return coming.get();
//    }

//    public Message readDataToMessage(byte[] datas) {
////        out.incrementAndGet();
//        if (datas == null) {
////            empty.getAndIncrement();
////                System.out.println("read null-----------------------------");
//            return null;
//        }
//        ByteBuf byteBuf = allocator.directBuffer();
//        try {
//            Message data = Message.read(datas, byteBuf);
//            byteBuf.release();
//            return data;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//    public Message readDataToMessage(TempResult result) {
////        out.incrementAndGet();
//        if (result == null){
//            return null;
//        }
//        byte[] datas = result.getDatas();
//        if (datas == null) {
//            empty.getAndIncrement();
////                System.out.println("read null-----------------------------");
//            return null;
//        }
////        ByteBuf byteBuf = allocator.directBuffer();
////        lock(parse);
//        try {
//            ByteBuf byteBuf = allocator.directBuffer(datas.length);
//            Message data = Message.read(datas, byteBuf);
//            byteBuf.release();
//
//            return data;
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.info("reader position "+result.getReaderPosition());
//            return null;
//        }finally {
//            datas = null;
////            unLock(parse);
//        }
//
//    }

    public void sync() {
        writeSync = true;
        try {
            writeBlock.sync();
            writePage.sync();
            readBlock.sync();
            readPage.sync();
            if (currentReadPage < currentWritePage){
                int tem = currentReadPage % MAX_BLOCK;
                int end = (currentWritePage-1) % MAX_BLOCK;
                if (tem == end){
                    return;
                }
                if (tem < end){
                    for (int i = 0;i<tem;i++){
                        blocks[i].close();
                    }
                }else {
                    for (int i = end;i < tem;i++){
                        blocks[i].close();
                    }
                }

            }
        }finally {
            writeSync = false;

        }



    }
}
