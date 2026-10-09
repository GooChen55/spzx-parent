package com.spzx.common.redis.cache;

import com.spzx.common.redis.annotation.GuiguCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 切面类：  AOP
 * 对目标方法进行功能扩展。目的，为了提高查询数据效率。
 */
@Slf4j
@Component
@Aspect //声明切面类
public class GuiguCacheAspect {

    @Autowired
    RedisTemplate redisTemplate;

    //@SneakyThrows
    @Around(value = "@annotation(guiguCache)")
    public Object guiguCache(ProceedingJoinPoint joinPoint, GuiguCache guiguCache) throws Throwable {

        return joinPoint.proceed();
    }
}
