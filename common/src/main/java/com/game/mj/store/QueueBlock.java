package com.game.mj.store;

import com.game.mj.model.ReadBlockInfo;
import io.netty.util.concurrent.EventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Cleaner;
import sun.misc.Contended;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author zheng
 */
public class QueueBlock {
    private AtomicBoolean lock = new AtomicBoolean(false);
    private static AtomicInteger releaseCount = new AtomicInteger(0);
    private void unLock(AtomicBoolean lock) {

//        lock.set(false);
    }


    private void lock(AtomicBoolean lock) {
//        long start = System.currentTimeMillis();
//        while (true) {
//            if (lock.compareAndSet(false, true)) {
////                System.out.println("cat lock "+(System.currentTimeMillis() - start));
//                return;
//            }
//            LockSupport.parkNanos(UnLockQueue.THREAD_PARK_NANOS);
//        }

    }
    @Contended
    private volatile int pageIndex;
//    @Contended
//    private volatile SoftReference<BlockInfo> softReference = new SoftReference<>(new BlockInfo());
    private static Logger logger = LoggerFactory.getLogger(QueueBlock.class);
    private PageIndex writerIndex;
    private PageIndex readerIndex;
    private RandomAccessFile accessFile;
    private FileChannel fileChannel;
    @Contended
    private volatile ByteBuffer byteBuffer;

    private MappedByteBuffer mappedByteBuffer;
    private SimpleRecycle simpleRecycle = SimpleRecycle.getInstance();
    private String path;
    public static final Integer END = -1;
    public static final int BLOCK_SIZE =  64 * 2 * 1024 * 1024;//32MB


    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setReaderIndex(PageIndex readerIndex) {
        this.readerIndex = readerIndex;
    }
    public boolean isEnd(int value){
        return value == END;
    }
    public ByteBuffer duplicateFromMapper(){
        return mappedByteBuffer.duplicate();
    }
    public void setPath(String path){

        this.path = path;
    }


    /**
     * 获取下一个长度
     * @return
     */
    public int getNextLength(){
        int readerPosition = readerIndex.getReadPosition();
        if (BLOCK_SIZE - readerPosition < 4){
            readerPosition = 0;
        }
        mappedByteBuffer.position(readerPosition);
        readerIndex.setReadPosition(readerPosition);
        int length = mappedByteBuffer.getInt(readerPosition);
        if (length == END){
            readerPosition = 0;
            mappedByteBuffer.position(readerPosition);
            readerIndex.setReadPosition(readerPosition);
            length = mappedByteBuffer.getInt(readerPosition);
        }
        return length;
    }

    /**
     * 获取下一个长度
     * @return
     */
    public BlockInfo getNextReadInfo(int readerPosition,int writerIndex,int pageIndex){
        lock(lock);
        try {
            if (readerPosition == writerIndex){
                BlockInfo blockInfo = BlockInfo.getInstance();
                blockInfo.setBaseInfo(writerIndex,readerPosition,pageIndex);
//                BlockInfo blockInfo = new BlockInfo(writerIndex,readerPosition,pageIndex);
                blockInfo.setStartReadPositon(0);
//                readerPosition = 0;
                blockInfo.setPageIndex(++pageIndex);
                return blockInfo;
            }
//            BlockInfo blockInfo = new BlockInfo(writerIndex,readerPosition,pageIndex);
            BlockInfo blockInfo = BlockInfo.getInstance();
            blockInfo.setBaseInfo(writerIndex,readerPosition,pageIndex);
            int length;
            if (readerPosition < writerIndex){
                if (writerIndex - readerPosition < 4){
                    blockInfo.setUnReach(true);
                    return blockInfo;
                }
                mappedByteBuffer.position(readerPosition);
                length = mappedByteBuffer.getInt();
                blockInfo = getNextRotateInfo(length, blockInfo, mappedByteBuffer, readerPosition, writerIndex);
                if (blockInfo.isUnReach()){
                    return blockInfo;
                }
            }else {
                if (BLOCK_SIZE - readerPosition < 4){
                    readerPosition = 0;

                }
                mappedByteBuffer.position(readerPosition);
                length = mappedByteBuffer.getInt(readerPosition);
                if (length == END){
                    readerPosition = 0;
                }
                blockInfo = getNextRotateInfo(length,blockInfo,mappedByteBuffer,writerIndex,readerPosition);
                if (blockInfo.isUnReach()){
                    return blockInfo;
                }
            }

            blockInfo.setStartReadPositon(readerPosition);
            int temp = readerPosition + length + 4;
            if (temp > BLOCK_SIZE){

                temp = temp - BLOCK_SIZE;
//                logger.info("bigger size "+readerPosition +" incement"+(length + 4) + " and tmp "+temp + blockInfo);
            }
            blockInfo.setLength(length);
            blockInfo.setAfterReaderIndex(temp);
            return blockInfo;
        }catch (Exception e){
            e.printStackTrace();
//            System.out.println("here is reader position "+readerPosition +" writer position "+writerIndex);
            return null;
        }finally {
            unLock(lock);
        }

    }
    public BlockInfo nextBlockInfo(int pageIndex){
        BlockInfo blockInfo = BlockInfo.getInstance();
//                BlockInfo blockInfo = new BlockInfo(writerIndex,readerPosition,pageIndex);
        blockInfo.setStartReadPositon(0);
//                readerPosition = 0;
        blockInfo.setPageIndex(++pageIndex);
        blockInfo.setUnReach(true);
        return blockInfo;
    }
    public SearchDataResult searchDataResult(int readerPosition,int writerIndex,int pageIndex,SearchDataResult searchDataResult){
        if (readerPosition >= writerIndex){

            searchDataResult.setBlockInfo(nextBlockInfo(pageIndex));
            return searchDataResult;
        }
        BlockInfo blockInfo = new BlockInfo();
        blockInfo.setBaseInfo(writerIndex,readerPosition,pageIndex);
        int length;
        if (readerPosition < writerIndex){
            if (writerIndex - readerPosition < 4){
                searchDataResult.setBlockInfo(nextBlockInfo(pageIndex));
                return searchDataResult;
            }
            mappedByteBuffer.position(readerPosition);
            length = mappedByteBuffer.getInt();
            if (mappedByteBuffer.capacity() < length || length == END){
                searchDataResult.setBlockInfo(nextBlockInfo(pageIndex));
                return searchDataResult;
            }
            blockInfo.setStartReadPositon(readerPosition);
            int temp = readerPosition + length + 4;
            blockInfo.setLength(length);
            byte[] data = new byte[length];
            mappedByteBuffer.get(data);
            searchDataResult.setDatas(data);
            blockInfo.setAfterReaderIndex(temp);
            searchDataResult.setBlockInfo(blockInfo);
            return searchDataResult;
        }else {

            blockInfo.setUnReach(true);
            return searchDataResult;
        }


    }
    /**
     * 获取下一个长度
     * @return
     */
    public BlockInfo getNextReadInfo2(int readerPosition,int writerIndex,int pageIndex){

        try {
            if (readerPosition >= writerIndex){
                BlockInfo blockInfo = BlockInfo.getInstance();
                blockInfo.setBaseInfo(writerIndex,readerPosition,pageIndex);
//                BlockInfo blockInfo = new BlockInfo(writerIndex,readerPosition,pageIndex);
                blockInfo.setStartReadPositon(0);
//                readerPosition = 0;
                blockInfo.setPageIndex(++pageIndex);
                return blockInfo;
            }
//            BlockInfo blockInfo = new BlockInfo(writerIndex,readerPosition,pageIndex);
            BlockInfo blockInfo = new BlockInfo();
            blockInfo.setBaseInfo(writerIndex,readerPosition,pageIndex);
            int length;
            if (readerPosition < writerIndex){
                if (writerIndex - readerPosition < 4){
                    blockInfo.setUnReach(true);
                    return blockInfo;
                }
                mappedByteBuffer.position(readerPosition);
                length = mappedByteBuffer.getInt();
                blockInfo = getNextRotateInfo1(length, blockInfo, mappedByteBuffer, readerPosition, writerIndex);
                if (blockInfo.isUnReach()){
                    return blockInfo;
                }
            }else {

                blockInfo.setUnReach(true);
                return blockInfo;
            }

            blockInfo.setStartReadPositon(readerPosition);
            int temp = readerPosition + length + 4;
            blockInfo.setLength(length);
            blockInfo.setAfterReaderIndex(temp);
            return blockInfo;
        }catch (Exception e){
            e.printStackTrace();
//            System.out.println("here is reader position "+readerPosition +" writer position "+writerIndex);
            return null;
        }

    }
    /**
     * 获取下一个长度
     * @return
     */
    public BlockInfo getNextReadInfo1(int readerPosition,int writerIndex,int pageIndex){
        lock(lock);
        try {
            if (readerPosition == writerIndex){
                BlockInfo blockInfo = BlockInfo.getInstance();
                blockInfo.setBaseInfo(writerIndex,readerPosition,pageIndex);
//                BlockInfo blockInfo = new BlockInfo(writerIndex,readerPosition,pageIndex);
                blockInfo.setStartReadPositon(0);
//                readerPosition = 0;
                blockInfo.setPageIndex(++pageIndex);
                return blockInfo;
            }
//            BlockInfo blockInfo = new BlockInfo(writerIndex,readerPosition,pageIndex);
            BlockInfo blockInfo = new BlockInfo();
            blockInfo.setBaseInfo(writerIndex,readerPosition,pageIndex);
            int length;
            if (readerPosition < writerIndex){
                if (writerIndex - readerPosition < 4){
                    blockInfo.setUnReach(true);
                    return blockInfo;
                }
                mappedByteBuffer.position(readerPosition);
                length = mappedByteBuffer.getInt();
                blockInfo = getNextRotateInfo(length, blockInfo, mappedByteBuffer, readerPosition, writerIndex);
                if (blockInfo.isUnReach()){
                    return blockInfo;
                }
            }else {
                if (BLOCK_SIZE - readerPosition < 4){
                    readerPosition = 0;

                }
                mappedByteBuffer.position(readerPosition);
                length = mappedByteBuffer.getInt(readerPosition);
                if (length == END){
                    readerPosition = 0;
                }
                blockInfo = getNextRotateInfo(length,blockInfo,mappedByteBuffer,writerIndex,readerPosition);
                if (blockInfo.isUnReach()){
                    return blockInfo;
                }
            }

            blockInfo.setStartReadPositon(readerPosition);
            int temp = readerPosition + length + 4;
            if (temp > BLOCK_SIZE){

                temp = temp - BLOCK_SIZE;
//                logger.info("bigger size "+readerPosition +" incement"+(length + 4) + " and tmp "+temp + blockInfo);
            }
            blockInfo.setLength(length);
            blockInfo.setAfterReaderIndex(temp);
            return blockInfo;
        }catch (Exception e){
            e.printStackTrace();
//            System.out.println("here is reader position "+readerPosition +" writer position "+writerIndex);
            return null;
        }finally {
            unLock(lock);
        }

    }
    private BlockInfo getNextRotateInfo(int length, BlockInfo blockInfo, MappedByteBuffer mappedByteBuffer, int readerPosition, int writerIndex){
        if (length == END){
            blockInfo.setPageIndex(++pageIndex);
            mappedByteBuffer.position(readerPosition);
            blockInfo.setUnReach(true);
            return blockInfo;
        }
        blockInfo.setLength(length);
        if (readerPosition < writerIndex){
            if (writerIndex - readerPosition - length < 4){
                blockInfo.setPageIndex(++pageIndex);
//                byteBuffer.position(readerPosition);
                blockInfo.setUnReach(true);
            }
        }else {
            if (BLOCK_SIZE + writerIndex - readerPosition - length < 4){
                blockInfo.setPageIndex(++pageIndex);
//                byteBuffer.position(readerPosition);
                blockInfo.setUnReach(true);
            }
        }

        return blockInfo;
    }
    private BlockInfo getNextRotateInfo1(int length, BlockInfo blockInfo, MappedByteBuffer mappedByteBuffer, int readerPosition, int writerIndex){
        if (length == END){
            blockInfo.setPageIndex(++pageIndex);
            mappedByteBuffer.position(readerPosition);
            blockInfo.setUnReach(true);
            return blockInfo;
        }
        blockInfo.setLength(length);
        if (readerPosition < writerIndex){
            if (writerIndex - readerPosition - length < 4){
                blockInfo.setPageIndex(++pageIndex);
//                byteBuffer.position(readerPosition);
                blockInfo.setUnReach(true);
            }
        }else {
            blockInfo.setPageIndex(++pageIndex);
//                byteBuffer.position(readerPosition);
            blockInfo.setUnReach(true);
        }

        return blockInfo;
    }

    /**
     * 根据blockinfo 来写数据
     * @param datas
     * @param blockInfo
     */
    public int writeData(byte[] datas,BlockInfo blockInfo){
        lock(lock);
        try {
            return writeRotate2(datas,blockInfo.getStartwritePosition());
        }finally {
            unLock(lock);
        }

    }
    /**
     * 根据blockinfo 来写数据
     * @param datas
     * @param blockInfo
     */
    public int writeData2(byte[] datas){
        lock(lock);
        try {
            return write(datas);
        }finally {
            unLock(lock);
        }

    }
    public void setWriterIndex(PageIndex writerIndex) {
        this.writerIndex = writerIndex;
    }

    public boolean checkByteBufferOpen(){
        return byteBuffer != null;
    }

    public QueueBlock(PageIndex writerIndex, PageIndex readerIndex, String path) {
        this.writerIndex = writerIndex;
        this.readerIndex = readerIndex;
        this.path = path;
        try {
            File file = new File(path);
            if (!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            this.accessFile = new RandomAccessFile(file,"rw");
            this.fileChannel = accessFile.getChannel();
            this.mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,BLOCK_SIZE);
            this.byteBuffer = mappedByteBuffer.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public QueueBlock(PageIndex writerIndex, PageIndex readerIndex, String path,int pageIndex) {
        this(writerIndex,readerIndex,path);
        this.pageIndex = pageIndex;
    }
    public ByteBuffer duplicateByteBuffer(){
        try {
            if (byteBuffer != null){
                return byteBuffer.duplicate();
            }
            this.accessFile = new RandomAccessFile(path,"rw");
            this.fileChannel = accessFile.getChannel();
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE,0,BLOCK_SIZE);
            return mappedByteBuffer.load();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("cannot open the file --------------");
        }

    }

    public void load(){
        if (mappedByteBuffer != null){
            mappedByteBuffer.load();
        }
    }
    public String getPath() {
        return path;
    }

    public void openChannelBuffer(QueueBlock queueBlock, PageIndex readerIndex,PageIndex writerIndex){
        try {
//            System.out.println("here is the path "+path);
//            this.path = queueBlock.getPath();
            this.readerIndex = readerIndex;
            this.writerIndex = writerIndex;
            this.accessFile = new RandomAccessFile(path,"rw");
            this.fileChannel = accessFile.getChannel();
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE,0,BLOCK_SIZE);
            if (this.byteBuffer == null ){
                if (queueBlock == null){

                    this.byteBuffer = mappedByteBuffer.load();
                }else {
                    this.byteBuffer = queueBlock.duplicateFromMapper();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public QueueBlock(PageIndex writerIndex,PageIndex readerIndex,  ByteBuffer byteBuffer, FileChannel fileChannel, String path,MappedByteBuffer mappedByteBuffer) {
        this.writerIndex = writerIndex;
        this.readerIndex = readerIndex;
        this.byteBuffer = byteBuffer;
        this.fileChannel = fileChannel;
        this.path = path;
        this.mappedByteBuffer = mappedByteBuffer;
//        this.byteBuffer = this.mappedByteBuffer.duplicate();
    }
    public QueueBlock(PageIndex writerIndex,PageIndex readerIndex,  RandomAccessFile accessFile, FileChannel fileChannel, ByteBuffer byteBuffer, MappedByteBuffer mappedByteBuffer, String path) {
        this.writerIndex = writerIndex;
        this.readerIndex = readerIndex;
        this.accessFile = accessFile;
        this.fileChannel = fileChannel;
        this.mappedByteBuffer = mappedByteBuffer;
        this.byteBuffer = byteBuffer;
        this.path = path;
    }
    public void putEND(){
        int writerPosition = writerIndex.getWriterPosition();
//        if (writerPosition > BLOCK_SIZE){
//            writerPosition = BLOCK_SIZE - 4;
//        }
        this.mappedByteBuffer.position(writerPosition);
        this.mappedByteBuffer.putInt(END);
    }

    /**
     * 环形写
     * @param datas
     */
    public void writeRotate(byte[] datas){
//        int len = datas.length;
//        int increment = len + 4;
//        int writerPosition = writerIndex.getWriterPosition();
//        if (writerPosition > BLOCK_SIZE){
//            throw new IllegalArgumentException("writer position is not write "+writerPosition);
//        }
//        mappedByteBuffer.position(writerPosition);
////        System.out.println("here is writer position "+writerPosition + " and length "+increment);
//        int totalLength = writerPosition + increment;
//
//
//        if (totalLength > BLOCK_SIZE ){
//            if (BLOCK_SIZE - writerPosition < 4){
//                mappedByteBuffer.position(0);
//                mappedByteBuffer.putInt(len);
//                mappedByteBuffer.put(datas);
//                writerIndex.setWriterPosition(increment);
////                System.out.println("herelw00000000000000000000000");
////                writerPosition = increment;
////                System.out.println("werite position "+writerIndex.getWriterPosition());
//                return;
//            }
//            int tempLength = BLOCK_SIZE -4 - writerPosition;
//            mappedByteBuffer.putInt(len);
////            System.out.println("here is better lock size start at "+writerPosition + " and length "+len + " and read position "+readerIndex.getReadPosition());
//            byte[] temp = new byte[tempLength];
//            System.arraycopy(datas,0,temp,0,tempLength);
////            System.out.println("here is ready to out of message "+writerPosition +" temp length "+tempLength + " capacity "+byteBuffer.capacity());
//            mappedByteBuffer.put(temp);
//            mappedByteBuffer.position(0);
//            byte[] rest = new byte[len - tempLength];
//            System.arraycopy(datas,tempLength,rest,0,len-tempLength);
//            mappedByteBuffer.put(rest);
//            writerIndex.setWriterPosition(len - tempLength);
//            System.out.println("here is ---------------");
////            writerPosition = writerIndex.getWriterPosition();
////            System.out.println("here is write at "+writerPosition + " data length "+len + " after writer position "+writerIndex.getWriterPosition() + " reader index "+readerIndex.getReadPosition());
//
//        }else {
//
//            mappedByteBuffer.putInt(len);
//            mappedByteBuffer.put(datas);
//
//            writerIndex.setWriterPosition(totalLength);
////            writerPosition = writerIndex.getWriterPosition();
////            System.out.println("here is write at "+writerPosition + " data length "+len + " after writer position "+writerIndex.getWriterPosition() + " reader index "+readerIndex.getReadPosition());
//
//        }
        int writePosition = writeRotate2(datas, writerIndex.getWriterPosition());
        writerIndex.setWriterPosition(writePosition);

    }

    /**
     * 环形写
     * @param datas
     */
    public int writeRotate2(byte[] datas,int writerPosition){
        int len = datas.length;
        int increment = len + 4;

        if (writerPosition > BLOCK_SIZE){
            throw new IllegalArgumentException("writer position is not write "+writerPosition);
        }
        mappedByteBuffer.position(writerPosition);
        int totalLength = writerPosition + increment;
//
//        logger.info("here is offer data at "+writerPosition + " page index "+pageIndex);
        if (totalLength > BLOCK_SIZE ){
            if (BLOCK_SIZE - writerPosition < 4){
                mappedByteBuffer.position(0);
                mappedByteBuffer.putInt(len);
//                System.out.println("here is better lock size start at "+writerPosition + " and length "+len + " and read position "+readerIndex.getReadPosition());
                mappedByteBuffer.put(datas);
                return increment;
            }
            int tempLength = BLOCK_SIZE -4 - writerPosition;
//            mappedByteBuffer.flip();
            mappedByteBuffer.putInt(len);

            byte[] temp = simpleRecycle.getWithLock(tempLength);
            System.arraycopy(datas,0,temp,0,tempLength);
            mappedByteBuffer.put(temp);
            mappedByteBuffer.position(0);
            byte[] rest = simpleRecycle.getWithLock(len - tempLength);
//            byte[] rest = new byte[len - tempLength];
            System.arraycopy(datas,tempLength,rest,0,len-tempLength);
            mappedByteBuffer.put(rest);
//            mappedByteBuffer.flip();
//            logger.info("here is better lock size start at "+writerPosition + " and length "+len + " and read position "+readerIndex.getReadPosition() + " temp length "+tempLength +" and "+(len-tempLength));
//            writerIndex.setWriterPosition(len - tempLength);
            return len - tempLength;
//
        }else {

            mappedByteBuffer.putInt(len);
            mappedByteBuffer.put(datas);
//            System.out.println("here is better lock size start at "+writerPosition + " and length "+len + " and read position "+readerIndex.getReadPosition());
            writerIndex.setWriterPosition(totalLength);
//            mappedByteBuffer.flip();
            return totalLength;
        }

    }
    public int write(byte[] datas){
        int len = datas.length;
        int increment = len + 4;
        int writerPosition = writerIndex.getWriterPosition();
        mappedByteBuffer.position(writerPosition);
        if (mappedByteBuffer.capacity() < increment){
           return -1;
        }
        mappedByteBuffer.putInt(len);
        mappedByteBuffer.put(datas);
        writerIndex.setWriterPosition(increment + writerPosition);
        return increment;
    }
    // 数据结构是len + data[]
    // 所以increment = len + 4
    public boolean isSpaceAvaibale(int len){
        int increment = len + 4;
        int writePosition = writerIndex.getWriterPosition();
        if (writePosition > BLOCK_SIZE){
            System.out.println("here is start bigger ");
        }
        return BLOCK_SIZE >= increment + writePosition + 4;
    }
    // 所以increment = len + 4
    public boolean isRotateSpaceAvaibale(int len){
        int reader = readerIndex.getReadPosition();
        int writer = writerIndex.getWriterPosition();
//        if (writer == 4070){
//            System.out.println("here is comming ------------");
//        }
        int increment = len + 4;
        if (reader <= writer){
//            System.out.println("here is writer "+writer + " max "+BLOCK_SIZE);
            int temp = BLOCK_SIZE -writer + reader;

            return temp >= increment  + 4;
        }else {
            int tem = reader - writer ;

            return  tem >= increment + 4;
        }




    }

    public PageIndex getWriterIndex() {
        return writerIndex;
    }

    public PageIndex getReaderIndex() {
        return readerIndex;
    }

    public boolean rotateEnd(){
        int readPosition = readerIndex.getReadPosition();
        int writerPosition = readerIndex.getWriterPosition();
//        if (readPosition > 900){
//            System.out.println("here is start to out");
//        }
        if (readPosition == writerPosition){
            return true;
        }
        if (readPosition > BLOCK_SIZE){
            return true;
        }
        if (BLOCK_SIZE - readPosition < 4){
            return true;
        }
//        if (readPosition > writerPosition){
//            readPosition = writerPosition;
//        }
//        readPosition = readPosition == BLOCK_SIZE ? 0 : readPosition;
//
//        return byteBuffer.getInt(readPosition) == END;
        return false;
    }
    public static Integer getReleaseCount(){
        return releaseCount.get();
    }
    public void laizyClose(EventExecutor executor){
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                logger.info("here is laizycomming");
                close();
                logger.info("here is after laizycomming");
            }
        },40, TimeUnit.MILLISECONDS);
    }
    public void close() {
        try {
            Long time = System.currentTimeMillis();
            if (mappedByteBuffer == null) {
                logger.info("mapperbuerr null");
                return;
            }
            releaseCount.getAndIncrement();
            sync();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    try {

                        logger.info("close now");
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner");
                        getCleanerMethod.setAccessible(true);
                        Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(mappedByteBuffer);
                        cleaner.clean();
                    } catch (Exception e) {
                        logger.error("close fqueue block file failed", e);
                    }
                    return null;
                }
            });
            mappedByteBuffer = null;
            byteBuffer = null;
            fileChannel.close();
            accessFile.close();

            logger.info("file channle close--------"+(System.currentTimeMillis() - time));
        } catch (IOException e) {
            logger.error("close fqueue block file failed", e);
        }
    }
    public boolean end(){
        int readPosition = readerIndex.getReadPosition();
        return readPosition > 0  && mappedByteBuffer.getInt(readPosition) == END;
    }
    public void writeEnd(){
        mappedByteBuffer.position(writerIndex.getWriterPosition());
        mappedByteBuffer.putInt(END);
    }
    public void initReaderIndex(){
        readerIndex.setReadPosition(0);
    }
    public byte[] readWithIndex(){
        return read(readerIndex.getReadPosition());
    }

    /**
     * 获取文件中某个位置的信息
     * @param readPosition
     * @return
     */
    public byte[] read(int readPosition){
        byte[] datas;
        mappedByteBuffer.position(readPosition);
        int dataLength = mappedByteBuffer.getInt();
        if (dataLength <= 0){
            return null;
        }
        datas = new byte[dataLength];
        mappedByteBuffer.get(datas);
        readerIndex.setReadPosition(readPosition + dataLength + 4);
        return datas;
    }
    public ReadBlockInfo readBlockInfo(int readPosition){
        byte[] datas;
        mappedByteBuffer.position(readPosition);
        int dataLength = mappedByteBuffer.getInt();
        if (dataLength <= 0){
            return null;
        }
        ReadBlockInfo readBlockInfo = new ReadBlockInfo();
        datas = new byte[dataLength];
        mappedByteBuffer.get(datas);
        readerIndex.setReadPosition(readPosition + dataLength + 4);
        readBlockInfo.setData(datas);
        readBlockInfo.setEndReadPosition(readerIndex.getReadPosition());
        return readBlockInfo;
    }

    /**
     * 环形读取数据
     * @return
     */
    public byte[] readRotate(){
        byte[] datas ;
        int readerPosition = readerIndex.getReadPosition();
        int writerPosition = writerIndex.getWriterPosition();
        System.out.println("here istart at readpositon "+readerPosition );
        mappedByteBuffer.position(readerPosition);
        int dataLength = mappedByteBuffer.getInt();
        System.out.println("here istart at readpositon "+readerPosition + " to get data length "+dataLength);
//        System.out.println("here is reader positon "+readerPosition + " data length "+dataLength);
        if (dataLength <= 0){
            return null;
        }
//

        if (readerPosition < writerPosition){
            if (writerPosition - readerPosition < dataLength){
                return null;
            }
            datas = new byte[dataLength];
            mappedByteBuffer.get(datas);
            readerIndex.setReadPosition(readerPosition + dataLength + 4);
            return datas;

        }else {
            if (BLOCK_SIZE > (readerPosition + dataLength)){
                datas = new byte[dataLength];
                mappedByteBuffer.get(datas);
                readerIndex.setReadPosition(readerPosition + dataLength + 4);
                return datas;

            }else {
                if ((BLOCK_SIZE +writerPosition - readerPosition) < dataLength){
                    return null;
                }
                datas = new byte[dataLength];
                int tempLength = BLOCK_SIZE - readerPosition;
                byte[] temp = new byte[tempLength];
                mappedByteBuffer.get(temp);
                mappedByteBuffer.position(0);
                byte[] rest = new byte[dataLength - tempLength];
                mappedByteBuffer.get(rest);
                System.arraycopy(temp,0,datas,0,tempLength);
                System.arraycopy(rest,0,datas,tempLength,dataLength-tempLength);
                readerIndex.setReadPosition(dataLength - tempLength);
                return datas;
            }

        }


    }

    /**
     * 判断是否读完了数据
     * @param readerPosition
     * @param dataLength
     * @return
     */
    public boolean readEnd(int readerPosition,int dataLength){
        int writerPosition = readerIndex.getWriterPosition();
//
        if (readerPosition == writerPosition){
            return true;
        }
//
        boolean next = false;

//

        int remaining = 0;
        if (readerPosition < writerPosition){
            remaining = writerPosition - readerPosition - 4;

        }else {
            remaining = writerPosition + BLOCK_SIZE - readerPosition - 4;
//
        }
        return remaining > dataLength;
    }
    public byte[] read(int readerPosition,int dataLength){
        return read4(dataLength);
//        byte[] datas ;
//        int writerPosition = readerIndex.getWriterPosition();
////
//        if (readerPosition == writerPosition){
//            return null;
//        }
////        System.out.println("here is read position ------------------"+readerPosition + " and writer position =========="+writerPosition);
//        boolean next = false;
////
//        if (BLOCK_SIZE - readerPosition < 4){
//            readerPosition = 0;
//        }
//        int remaining = 0;
//        if (readerPosition < writerPosition){
//            remaining = writerPosition - readerPosition - 4;
//
//        }else {
//            remaining = writerPosition + BLOCK_SIZE - readerPosition - 4;
////
//        }
////
////        if (dataLength > BLOCK_SIZE){
////            throw new RuntimeException("data length no wright and reader position "+ readerPosition + " length "+ dataLength + " writer positioin "+writerPosition);
////        }
////        byteBuffer.position(readerPosition);
//        if (remaining < dataLength){
////            System.out.println("here is reader position "+readerPosition + " remaining "+remaining + " and data length"+dataLength+" here is reader "+readerPosition +" here is size "+QueueBlock.BLOCK_SIZE + " writer position "+writerPosition);
//            datas = new byte[dataLength];
//            int tempLength = BLOCK_SIZE - readerPosition ;
//            byte[] temp = new byte[tempLength];
//            byteBuffer.get(temp);
//            byte[] rest =new byte[dataLength -tempLength - 4];
//            byteBuffer.position(0);
//            byteBuffer.get(rest);
//            readerIndex.setReadPosition(dataLength-tempLength);
//            System.arraycopy(temp,0,datas,0,tempLength);
//            System.arraycopy(rest,0,datas,tempLength,dataLength-tempLength);
////            System.out.println("here is last reader position"+readerIndex.getReadPosition()+" and current position "+(dataLength-tempLength));
//
////            throw new RuntimeException("this block have not wright data");
//            return datas;
////            return null;
//        }else {
//            if ( BLOCK_SIZE - 4 - readerPosition < dataLength){
//
////                byteBuffer.position(readerPosition + 4);
//                int tempLength = BLOCK_SIZE - readerPosition - 4;
//                byte[] temps = new byte[tempLength];
//                byteBuffer.get(temps);
//                byteBuffer.position(0);
//                byte[] rest = new byte[dataLength - tempLength];
//                byteBuffer.get(rest);
//                datas = new byte[dataLength];
//
//                System.arraycopy(temps,0,datas,0,tempLength);
//                System.arraycopy(rest,0,datas,tempLength,dataLength-tempLength);
//                readerIndex.setReadPosition(dataLength-tempLength);
////
////                System.out.println("here is read data at "+readerPosition + " and length "+dataLength + " writer position "+writerPosition + " and new reader position "+readerIndex.getReadPosition());
//
//                return datas;
//            }
//            if (readerPosition +dataLength +4 > BLOCK_SIZE){
//                throw new IllegalArgumentException("not write reader position "+readerPosition + " data length "+dataLength);
//            }
//            readerIndex.setReadPosition(readerPosition + dataLength + 4);
////
////            System.out.println("here is read data at "+readerPosition + " and length "+dataLength + " writer position "+writerIndex.getWriterPosition() + " and new reader position "+readerIndex.getReadPosition());
//            byteBuffer.position(readerPosition + 4);
//            if (dataLength > byteBuffer.remaining() || dataLength < 0){
//                throw new RuntimeException("datalength bigger than remain "+ dataLength +" remain "+ byteBuffer.remaining() +" writer position "+writerPosition + " reader position "+readerPosition);
//            }
//            datas = new byte[dataLength];
//            byteBuffer.get(datas);
//            return datas;
//
//
//
//
//        }

    }

    /**
     * 读取数据
     * @param readerPosition
     * @param writerPosition
     * @param dataLength
     * @return
     */
    public TempResult read2(int readerPosition, int writerPosition, int dataLength,TempResult tempResult){
        lock(lock);
        try {
            if (readerPosition == writerPosition){
                return null;
            }
//        TempResult tempResult = TempResult.getInstance();
//        tempResult.init();
            byte[] datas;
            if (BLOCK_SIZE - readerPosition < 4){
                readerPosition = 0;
            }
            int remaining ;
            if (readerPosition < writerPosition){
                remaining = writerPosition - readerPosition - 4;
            }else {
                remaining = writerPosition + BLOCK_SIZE - readerPosition - 4;
            }
//        logger.info("here is reader position "+readerPosition + " remaining "+remaining + " and data length"+dataLength+" here is reader "+readerPosition +" here is size "+QueueBlock.BLOCK_SIZE + " writer position "+writerPosition + " page index "+pageIndex);
//
            if (remaining < dataLength){
//
                throw new RuntimeException("not wright -----------");
//            datas = simpleRecycle.getWithLock(dataLength+4);
//            int tempLength = BLOCK_SIZE - readerPosition ;
//            byte[] temp = simpleRecycle.getWithLock(tempLength);
//            byteBuffer.get(temp);
//            byte[] rest = simpleRecycle.getWithLock(dataLength -tempLength - 4);
////            byte[] rest =new byte[dataLength -tempLength - 4];
//            byteBuffer.position(0);
//            byteBuffer.get(rest);
//            readerIndex.setReadPosition(dataLength-tempLength);
////            byte[] temp = SimpleRecycle.getTemp(readerPosition+4,tempLength,byteBuffer,byteBuf);
////            byte[] rest = SimpleRecycle.getTemp(0,dataLength -tempLength - 4,byteBuffer,byteBuf);
////            byteBuf.writeBytes(temp);
////            byteBuf.writeBytes(rest);
//            System.arraycopy(temp,0,datas,0,tempLength);
//            System.arraycopy(rest,0,datas,tempLength,dataLength-tempLength);
////
//            return datas;
//            return null;
            }else {
                if ( BLOCK_SIZE - 4 - readerPosition < dataLength){
                    mappedByteBuffer.position(readerPosition +4);
                    int tempLength = BLOCK_SIZE - readerPosition - 4;
                    byte[] temps = simpleRecycle.getWithLock(tempLength);
                    tempResult.setDatas(simpleRecycle.getWithLock(dataLength));
                    datas = tempResult.getDatas();
//
                    mappedByteBuffer.position(0);
                    byte[] rest = simpleRecycle.getWithLock(dataLength - tempLength);
//                byte[] rest = new byte[dataLength - tempLength];
                    mappedByteBuffer.get(rest);
//                byteBuf.writeBytes(temps);
//                byteBuf.writeBytes(rest);
                    System.arraycopy(temps,0,datas,0,tempLength);
                    System.arraycopy(rest,0,datas,tempLength,dataLength-tempLength);
                    readerIndex.setReadPosition(dataLength-tempLength);
//                logger.info("reader position "+readerPosition +"data length "+dataLength +" new position "+(dataLength - tempLength) + " and tempLength "+tempLength + " byte length "+datas.length + " data "+datas.toString());
                    return tempResult;

//
//                return datas;
                }
//            if (readerPosition +dataLength +4 > BLOCK_SIZE){
//                throw new IllegalArgumentException("not write reader position "+readerPosition + " data length "+dataLength);
//            }
                readerIndex.setReadPosition(readerPosition + dataLength + 4);
//
//            logger.info("here is read data at "+readerPosition + " and length "+dataLength + " writer position "+writerIndex.getWriterPosition() + " and new reader position "+readerIndex.getReadPosition()+ " page Index "+readerIndex.getIndex());
                mappedByteBuffer.position(readerPosition + 4);
//            if (dataLength > byteBuffer.remaining() || dataLength < 0){
//                throw new RuntimeException("datalength bigger than remain "+ dataLength +" remain "+ byteBuffer.remaining() +" writer position "+writerPosition + " reader position "+readerPosition);
//            }
                tempResult.setDatas(simpleRecycle.getWithLock(dataLength));
                datas = tempResult.getDatas();

                mappedByteBuffer.get(datas);
//            mappedByteBuffer.flip();
                return tempResult;
            }
        }finally {
            unLock(lock);
        }


    }
    /**
     * 读取数据
     * @param readerPosition
     * @param writerPosition
     * @param dataLength
     * @return
     */
    public TempResult read23(int readerPosition, int writerPosition, int dataLength,TempResult tempResult){
        lock(lock);
        try {
            if (readerPosition == writerPosition){
                return null;
            }
//        TempResult tempResult = TempResult.getInstance();
//        tempResult.init();
            byte[] datasn =read4(dataLength);
            tempResult.setDatas(datasn);
            tempResult.setReaderPosition(readerIndex.getReadPosition());
            return tempResult;
//        logger.info("here is reader position "+readerPosition + " remaining "+remaining + " and data length"+dataLength+" here is reader "+readerPosition +" here is size "+QueueBlock.BLOCK_SIZE + " writer position "+writerPosition + " page index "+pageIndex);
//

        }finally {
            unLock(lock);
        }


    }
    /**
     * 读取数据
     * @param readerPosition
     * @param writerPosition
     * @param dataLength
     * @return
     */
    public TempResult read33(int readerPosition, int writerPosition, int dataLength,TempResult tempResult){
        if (readerPosition == writerPosition){
            return null;
        }
//        TempResult tempResult = TempResult.getInstance();
//        tempResult.init();
        byte[] datasn =read4(dataLength);
        tempResult.setDatas(datasn);
        tempResult.setReaderPosition(readerIndex.getReadPosition());
        return tempResult;


    }

    /**
     * 读取数据
     * @param readerPosition
     * @param writerPosition
     * @param dataLength
     * @return
     */
    public byte[] read(int readerPosition,int writerPosition,int dataLength){
        byte[] datas ;
        if (readerPosition == writerPosition){
            return null;
        }
        if (BLOCK_SIZE - readerPosition < 4){
            readerPosition = 0;
        }
        int remaining = 0;
//        byteBuf.co
        if (readerPosition < writerPosition){
            remaining = writerPosition - readerPosition - 4;
        }else {
            remaining = writerPosition + BLOCK_SIZE - readerPosition - 4;
        }

        if (remaining < dataLength){
//            System.out.println("here is reader position "+readerPosition + " remaining "+remaining + " and data length"+dataLength+" here is reader "+readerPosition +" here is size "+QueueBlock.BLOCK_SIZE + " writer position "+writerPosition);
            datas = new byte[dataLength];
            int tempLength = BLOCK_SIZE - readerPosition ;
            byte[] temp = new byte[tempLength];
            mappedByteBuffer.get(temp);
            byte[] rest =new byte[dataLength -tempLength - 4];
            mappedByteBuffer.position(0);
            mappedByteBuffer.get(rest);
            readerIndex.setReadPosition(dataLength-tempLength);
            System.arraycopy(temp,0,datas,0,tempLength);
            System.arraycopy(rest,0,datas,tempLength,dataLength-tempLength);
//
            return datas;
//            return null;
        }else {
            if ( BLOCK_SIZE - 4 - readerPosition < dataLength){

                int tempLength = BLOCK_SIZE - readerPosition - 4;
                byte[] temps = new byte[tempLength];
                mappedByteBuffer.get(temps);
                mappedByteBuffer.position(0);
                byte[] rest = new byte[dataLength - tempLength];
                mappedByteBuffer.get(rest);
                datas = new byte[dataLength];

                System.arraycopy(temps,0,datas,0,tempLength);
                System.arraycopy(rest,0,datas,tempLength,dataLength-tempLength);
                readerIndex.setReadPosition(dataLength-tempLength);
//
                return datas;
            }
            if (readerPosition +dataLength +4 > BLOCK_SIZE){
                throw new IllegalArgumentException("not write reader position "+readerPosition + " data length "+dataLength);
            }
            readerIndex.setReadPosition(readerPosition + dataLength + 4);
//
//            System.out.println("here is read data at "+readerPosition + " and length "+dataLength + " writer position "+writerIndex.getWriterPosition() + " and new reader position "+readerIndex.getReadPosition());
            mappedByteBuffer.position(readerPosition + 4);
            if (dataLength > byteBuffer.remaining() || dataLength < 0){
                throw new RuntimeException("datalength bigger than remain "+ dataLength +" remain "+ byteBuffer.remaining() +" writer position "+writerPosition + " reader position "+readerPosition);
            }
            datas = new byte[dataLength];
            mappedByteBuffer.get(datas);
            return datas;




        }

    }
    public byte[] read4(int dataLength) {
        byte[] bytes;

        int readPosition = readerIndex.getReadPosition();
//        int writeNum = index.getWriteNum();
        int writePosition = readerIndex.getWriterPosition();
        if ( readPosition >= writePosition) {
            return null;
        }
        if (dataLength <= 0) {
            return null;
        }
        byteBuffer.position(readPosition+4);
//        int dataLength = byteBuffer.getInt();

        bytes = new byte[dataLength];
        byteBuffer.get(bytes);
        readerIndex.setReadPosition(readPosition + dataLength + 4);
//        index.putReadCounter(index.getReadCounter() + 1);
        return bytes;
    }
    public void sync() {
        if (mappedByteBuffer != null){
//            System.out.println("here is call the sync() method -----------------------------");
            mappedByteBuffer.force();
        }
    }


    /**
     * copy a new one
     * @return
     */
    public QueueBlock duplicate() {
        return new QueueBlock(writerIndex,readerIndex,this.path);
//        return new QueueBlock(this.writerIndex,this.readerIndex,this.accessFile,this.fileChannel,this.byteBuffer.duplicate(),this.mappedByteBuffer,this.path);
    }

    /**
     * copy a new one
     * @return
     */
    public QueueBlock simpleDuplicate() {
        return new QueueBlock(this.writerIndex,this.readerIndex,this.mappedByteBuffer.duplicate(),this.fileChannel,this.path,this.mappedByteBuffer);
    }
}
