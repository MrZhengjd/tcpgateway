package lock.workerthread;

/**
 * @author zheng
 */

public class Request {
    private final String name;
    private final int number;

    public Request(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public void execute() {
        System.out.println("welcome execute "+name +" number "+number);
    }
}
