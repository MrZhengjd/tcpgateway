import com.game.mj.concurrent.IGameEventExecutorGroup;
import com.game.mj.concurrent.LocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import io.netty.util.concurrent.Promise;

/**
 * @author zheng
 */
public class TestTimeOut {
    public static void main(String[] args) {
         PromiseUtil.safeExecute(IGameEventExecutorGroup.getInstance().selectByHash(1), new LocalRunner<Void>() {
            @Override
            public void task(Promise promise,Void voi) {
                try {
                    System.out.println("here is start to dojork ---");
                    Thread.sleep(10000);
                    int tem = 0;
                    tem ++;
                    System.out.println("tem "+tem);
                    System.out.println("is canceled "+promise.isCancelled());
                    promise.setSuccess(true);
                    System.out.println("here is start to dojork sdf ---");

                } catch (InterruptedException e) {
                    if (promise.isDone()){
                        System.out.println("timeout exception ");
                    }
                    e.printStackTrace();

                }
            }
        },null);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        PromiseUtil.safeExecute(IGameEventExecutorGroup.getInstance().selectByHash(2), new LocalRunner<Void>() {
            @Override
            public void task(Promise promise,Void vo) {
                System.out.println("here is welcome ---------");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                promise.setSuccess(true);
            }
        },null);
    }
}
