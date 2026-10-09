package com.spzx.product.service.impl;

import com.spzx.common.core.utils.StringUtils;
import com.spzx.product.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    StringRedisTemplate stringRedisTemplate; //SpringBoot自动化配置

    @Override
    public void testLock() {
        //第一版：上分布式锁（当上完锁之后如果出现异常，会导致锁无法释放）
//        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent("lock", "hello");
        //第二版：设置key的同时设过期时间（如果执行业务逻辑的时间笔锁的过期时间要长，会出现释放别人锁的情况）
//        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent("lock", "hello",3, TimeUnit.SECONDS);
        //第三版：使用UUID随机生成一个字符串作为锁的值（如果判断是否是自己的锁和释放锁分步完成，那么仍然会出现释放别人锁的情况）
        String lockValue = UUID.randomUUID().toString().replaceAll("-", "");
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent("lock", lockValue, 3, TimeUnit.SECONDS);
        //第四版：判断和删除锁使用Lua脚本，保证原子性
        if(flag){
            //上锁成功，执行业务逻辑
            try {
                // 查询Redis中的num值
                String value = (String) this.stringRedisTemplate.opsForValue().get("num");
                // 没有该值return
                if (StringUtils.isBlank(value)) {
                    return;
                }
                // 有值就转成成int
                int num = Integer.parseInt(value);
                // 把Redis中的num值+1
                this.stringRedisTemplate.opsForValue().set("num", String.valueOf(++num)); //  java 中  ++ 操作不是原子的。
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            } finally {
//                //获取Redis中锁的值
//                String redisLockValue = stringRedisTemplate.opsForValue().get("lock");
//                //判断是否是自己上的锁
//                if(lockValue.equals(redisLockValue)){
//                    //释放锁
//                    stringRedisTemplate.delete("lock");
//                }
                //设置Lua脚本
                String luaScript = """
                        if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end
                        """;
                //执行Lua脚本
                //创建RedisScript对象
                RedisScript<Boolean> redisScript = RedisScript.of(luaScript, Boolean.class);
                //设置Redis中的key
                List<String> keys = Arrays.asList("lock");
                stringRedisTemplate.execute(redisScript,keys,lockValue);
            }
        }else{
            try {
            //上锁失败，等待重试
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //重新尝试获取锁
            testLock();
        }


    }

}
