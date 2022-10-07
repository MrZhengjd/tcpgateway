package decorator;

/**
 * @author zheng
 */
public abstract class NoddleDecorator implements Noddle{
    private Double cost;
    private String name;
    protected Noddle noddle;

    public Noddle getNoddle() {
        return noddle;
    }

    public void setNoddle(Noddle noddle) {
        this.noddle = noddle;
    }

    @Override
    public abstract double cost();

    @Override
    public abstract String name();
}
