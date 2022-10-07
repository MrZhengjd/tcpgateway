package com.game.mj.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zheng
 */
public class ReadWriteLockOperate {
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public  void readLockOperation(Operation operation){
        readWriteLock.readLock().lock();
        try {
            operation.operate();
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            readWriteLock.readLock().unlock();
        }

    }

    public  <T> T readLockReturnOperation(ReturnOperate<T> operation){
        readWriteLock.readLock().lock();
        try {
            return operation.operate();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            readWriteLock.readLock().unlock();
        }

    }

    public  <T> T writeLockReturnOperation(ReturnOperate<T> operation){
        readWriteLock.writeLock().lock();
        try {
            return operation.operate();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            readWriteLock.writeLock().unlock();
        }

    }



    public  void writeLockOperation(Operation operation){
        readWriteLock.writeLock().lock();
        try {
            operation.operate();
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            readWriteLock.writeLock().unlock();
        }

    }

}
