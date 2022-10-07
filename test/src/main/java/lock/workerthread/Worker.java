package lock.workerthread;

/**
 * @author zheng
 */
public class Worker extends Thread {
    private final Channel channel;

    public Worker(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true){
            Request request = channel.takeRequest();
            if (request != null){
                request.execute();
            }
        }
    }
}
