package lock.workerthread;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author zheng
 */
public class Channel {
    private Request[] requests ;
    private static final int MAX= 1000;
    private int threadCount;
    private volatile int head;
    private volatile int tail;
    private Worker[] workers;
    private volatile int count = 0;
    public Channel(int threadCount) {
        this.threadCount = threadCount;
        workers = new Worker[threadCount];
        for (int i = 0;i< threadCount;i ++){
            workers[i] = new Worker(this);
        }
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        this.requests = new Request[MAX];
    }
    public void startWorkers(){
        for (int i = 0; i< threadCount;i++){
            workers[i].start();
        }
    }
    public synchronized void sendRequest(Request request) {
        while (count >= requests.length){
            try {
                wait();;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        requests[tail] = request;
        tail = (tail+1) % requests.length;
        count++;
        notifyAll();
    }

    public synchronized Request takeRequest() {
        while (count <= 0){
            try {
                wait();;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Request request = requests[head];
        head = (head +1)%requests.length;
        count --;
        notifyAll();
        return request;
    }
}
