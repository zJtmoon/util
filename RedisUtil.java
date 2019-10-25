package com.creditease.honeybot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     *
     * @param key   key
     * @param value 当前时间+超时时间 时间戳
     * @return
     */
    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {//对应setnx命令
            //可以成功设置,也就是key不存在
            return true;
        }

        //判断锁超时 - 防止原来的操作异常，没有运行解锁操作  防止死锁
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {//currentValue不为空且小于当前时间
            //获取上一个锁的时间value
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);//对应getset，如果key存在

            //假设两个线程同时进来这里，因为key被占用了，而且锁过期了。获取的值currentValue=A(get取的旧的值肯定是一样的),两个线程的value都是B,key都是K.锁时间已经过期了。
            //而这里面的getAndSet一次只会一个执行，也就是一个执行之后，上一个的value已经变成了B。只有一个线程获取的上一个值会是A，另一个线程拿到的值是B。
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                //oldValue不为空且oldValue等于currentValue，也就是校验是不是上个对应的商品时间戳，也是防止并发
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);//删除key
            }
        } catch (Exception e) {
            logger.error("[Redis分布式锁]git pull 解锁异常，{}", e);
        }
    }

    public void setStr(String key,String value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getStr(String key)
    {
        return redisTemplate.opsForValue().get(key);
    }

    public void hsetStr(String hKey,String field,String value)
    {
        redisTemplate.opsForHash().put(hKey, field, value);
    }

    public String hgetStr(String hKey,String field)
    {
        return redisTemplate.opsForHash().get(hKey, field).toString();
    }

    public Map<String,String> hgetAllString(String hKey)
    {
        Map<String,String> result = new HashMap<>();
        redisTemplate.opsForHash().keys(hKey).forEach(field->result.put(field.toString(), redisTemplate.opsForHash().get(hKey, field.toString()).toString()));
        return result;
    }

    public <K,V> Map<K,V> hgetAll(String hkey,Class<K> kClazz,Class<V> vClass)
    {
        Map<K,V> result = new HashMap<>();
        redisTemplate.opsForHash().keys(hkey).forEach(field->result.put(kClazz.cast(field), vClass.cast(redisTemplate.opsForHash().get(hkey, field.toString()))));
        return result;
    }

    public void hputAll(String hkey,Map map)
    {
        redisTemplate.delete(hkey);
        redisTemplate.opsForHash().putAll(hkey, map);
    }

    public void hdel(String map, String key) {
        redisTemplate.opsForHash().delete(map, key);
    }
}
