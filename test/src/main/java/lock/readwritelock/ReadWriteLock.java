package lock.readwritelock;

/**
 * @author zheng
 */
public class ReadWriteLock {
    private volatile int readCount;
    private volatile int writeCount;
    public synchronized void readLock() throws InterruptedException {
        while (writeCount > 0){
            wait();
        }
        readCount ++;
    }

    public synchronized void readUnLock() {
        readCount --;
        notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException{
        while (readCount > 0 || writeCount > 0){
            wait();
        }
        writeCount ++;
    }

    public synchronized void writeUnLock() {
        writeCount --;
        notifyAll();
    }
}
