package lock.readwritelock;

/**
 * @author zheng
 */
public class Data {
    private final byte[] datas;
    private final ReadWriteLock readWriteLock;

    public Data(byte[] datas, ReadWriteLock readWriteLock) {
        this.datas = datas;
        this.readWriteLock = readWriteLock;
    }

    public byte[] read()throws Exception{
        readWriteLock.readLock();
        try {
            return doRead();
        }finally {
            readWriteLock.readUnLock();
        }

    }

    private byte[] doRead() {
        return null;
    }

    public void write(byte data)throws Exception{
        readWriteLock.writeLock();
        try {
            doWrite(data);
        }finally {
            readWriteLock.writeUnLock();
        }
    }

    private void doWrite(byte data) {

    }
}
