package decorator;

/**
 * @author zheng
 */
public class TestDecorator {
    public static void main(String[] args) {
        Finery finery = new Finery();
        Finery big = new BigTrouser();
        Finery tshirt = new TShirts();
        finery.setPersion(big);
        big.setPersion(tshirt);
        finery.show();
    }
}
