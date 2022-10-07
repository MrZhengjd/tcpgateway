package lock.readwritelock;

import lock.readwritelock.Data;

/**
 * @author zheng
 */
public class WriteThread extends Thread {
    private Data date;

    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
