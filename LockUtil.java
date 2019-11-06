package com.zhangxinhulian.market.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Yang zeqi
 * @date 2019/8/16
 * @description: 写的毕竟简单 需要后续优化
 */
public class LockUtil {


    /**
     * 尝试获取分布式锁
     */
    public static boolean tryGetLock(RedisTemplate redisTemplate, String lockKey, String lockValue, int expireSeconds) {
        return redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 释放分布式锁
     */
    public static boolean delLock(RedisTemplate redisTemplate, String lockKey,String lockValue) {
        return redisTemplate.delete(lockKey);
    }

}
