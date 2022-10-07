package com.game.infrustructure.redis;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//@Component
@Configuration
@ConfigurationProperties(prefix="redis")
@PropertySource(value = "classpath:redis.properties",encoding = "utf-8")
@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
public class RedisUtil {
//    private static final RedisUtil REDIS_UTIL = new RedisUtil();
//    public static RedisUtil getRedisUtil(){
//        return RedisUtil.REDIS_UTIL;
//    }
    @Value("${redis.addr}")
    private String addr;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.auth}")
    private String auth;
    @Value("${redis.maxActive}")
    private int maxActive;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.maxWait}")
    private int maxWait;
    @Value("${redis.timeOut}")
    private int timeOut;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
//    @Autowired
//    private JedisPool jedisPool;
    @Bean
    public JedisPool pool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnBorrow(testOnBorrow);
        return new JedisPool(config,addr,port,maxWait);
    }
//    @Autowired
//    private JedisPool jedisPool;
//    public static String configPath0 = System.getProperty("user.dir")+ File.separator+"base"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator;


    //    public RedisUtil() {
////        PropertiesUtil propertiesUtil = new PropertiesUtil(configPath0+PropertiesConfig.redisProperties);
////        this.addr = propertiesUtil.getPropery("redis.addr");
////        this.port = Integer.parseInt(propertiesUtil.getPropery("redis.port"));
////        this.auth = propertiesUtil.getPropery("redis.auth");
////        this.auth = null;
////        this.maxIdle = Integer.parseInt(propertiesUtil.getPropery("redis.maxIdle"));
////        this.maxActive = Integer.parseInt(propertiesUtil.getPropery("redis.maxActive"));
////        this.maxWait = Integer.parseInt(propertiesUtil.getPropery("redis.maxWait"));
////        this.timeOut = Integer.parseInt(propertiesUtil.getPropery("redis.timeOut"));
////        this.testOnBorrow = Boolean.parseBoolean(propertiesUtil.getPropery("redis.testOnBorrow"));
//
//    }

    public String getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }

    public String getAuth() {
        return auth;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

//    public JedisPool getJedisPool() {
//        return jedisPool;
//    }
//
//    public void setJedisPool(JedisPool jedisPool) {
//        this.jedisPool = jedisPool;
//    }
}
