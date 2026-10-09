package com.spzx.common.redis.annotation;

import java.lang.annotation.*;

/**
 * 标记相关方法：
 * 作用：
 * 1.从缓存中获取数据，提高效率
 * 2.缓存没有数据，从数据库获取数据
 * 3.从数据库获取数据必须先获取分布式锁
 * 4.将数据库数据存放到缓存中，如果空数据也会存放，解决缓存穿透问题。
 * 5.释放分布式锁。保证相关加锁，解锁 原子性。
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GuiguCache {

    /**
     * 增加注解中方法（注解属性）通过注解属性指定放入缓存业务数据前缀、后缀
     */
    String prefix() default "data:";

}
