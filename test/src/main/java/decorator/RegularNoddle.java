package decorator;

/**
 * @author zheng
 */
public class RegularNoddle implements Noddle {
    @Override
    public double cost() {
        return 3;
    }

    @Override
    public String name() {
        return "egg";
    }
}
