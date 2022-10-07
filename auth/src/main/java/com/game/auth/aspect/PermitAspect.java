package com.game.auth.aspect;

import com.game.infrustructure.redis.JsonRedisManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author zheng
 */
@Aspect
@Configuration
public class PermitAspect {
    @Resource
    private JsonRedisManager jsonRedisManager;

    @Pointcut("@annotation(com.game.auth.annotation.Permit)")
    public void pointCut(){

    }
}
