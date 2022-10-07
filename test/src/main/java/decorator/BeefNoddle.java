package decorator;

/**
 * @author zheng
 */
public class BeefNoddle implements Noddle {
    @Override
    public double cost() {
        return 2;
    }

    @Override
    public String name() {
        return "beef";
    }
}
