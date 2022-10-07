package test;


import com.game.domain.relation.room.Room;
import org.redisson.api.RLocalCachedMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import test.redisson.RedissonConfig;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages ={"test"} )
public class TestApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TestApplication.class, args);
//        RLocalCachedMap<String ,Object> map = context.getBean(RLocalCachedMap.class);
        RedissonConfig redissonConfig = context.getBean(RedissonConfig.class);
        RLocalCachedMap<String, Room> map = redissonConfig.cachedMap();
        Room t = new Room();
        t.setRoomNum(1234);
        map.put("test",t);
        t.setRoomNum(234);
        System.out.println(map.get("test").getRoomNum());
//        map.put("test",t);
    }
}
