package decorator;

/**
 * @author zheng
 */
public class EggNoddleDecorator extends NoddleDecorator {
    @Override
    public double cost() {
        return noddle.cost() + 3;
    }

    @Override
    public String name() {
        return noddle.name() +"egg ";
    }
}
