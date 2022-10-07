import com.game.mj.generator.IdGenerator;
import com.game.mj.generator.IdGeneratorFactory;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author zheng
 */
public class TestIdGenerator {
    public static void main(String[] args) {
        IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
        Long aLong = idGenerator.generateIdFromServerId(11);
        Long timeMillis = idGenerator.generateIdFromServerId(31);
        System.out.println("generate time "+aLong +" currentTime "+timeMillis);
        SortedMap<Long,Object> map = new TreeMap<>();
        map.put(1l,"data");
        SortedMap<Long, Object> subMap = map.subMap(-1l, 2l);
        if (subMap == null){
            System.out.println("data is null");
        }
    }
}
