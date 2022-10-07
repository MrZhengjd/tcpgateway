import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zheng
 */
public class CycleTest {
    public static void main(String[] args) {
        AService as = new AServiceImpl();
        AService proxy = (AService) Proxy.newProxyInstance(as.getClass().getClassLoader(), as.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(as,args);
            }
        });

        BService bService = new BServiceImpl();
        ((BServiceImpl)bService).setaService(as);
        ((AServiceImpl)as).setbService(bService);
        proxy.a();


    }
}
