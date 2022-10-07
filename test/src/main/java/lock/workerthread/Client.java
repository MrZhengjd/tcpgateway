package lock.workerthread;

/**
 * @author zheng
 */
public class Client extends Thread{
    private final Channel channel;

    public Client(Channel channel) {
        this.channel = channel;
    }
    public void sendRequest(Request request){
        channel.sendRequest(request);
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0;i< 200;i++){
                Request request = new Request("test"+i,i);
                channel.sendRequest(request);
            }
        }
    }
}
