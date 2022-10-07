package hello.redisson;



import com.game.domain.relation.room.Room;
import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zheng
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.0.55:6379");
        RedissonClient client = Redisson.create(config);
        return client;
    }

    @Bean
    public RLocalCachedMap<String, Room> cachedMap(){
        LocalCachedMapOptions<String, Room> defaults = LocalCachedMapOptions.defaults();
        defaults.syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE);
        return redissonClient().getLocalCachedMap("test",defaults);


    }
}
