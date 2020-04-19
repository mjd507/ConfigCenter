package com.configcenter.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 项目使用时，需要 Import 进去
 *
 * @Import(value = {RedisOperation.class})
 * <p>
 * Created by mjd on 2020/4/14 19:40
 */
public class RedisOperation {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisOperation.class);

    StringRedisTemplate redisTemplate;

    public RedisOperation(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // for test
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ===================== String =====================

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public boolean setExpire(String key, long expire) {
        Boolean success = redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        return success != null && success;
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Integer getInteger(String key) {
        String s = redisTemplate.opsForValue().get(key);
        if (s == null || s.trim().equals("")) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ignore) {
        }
        return null;
    }

    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire != null ? expire : 0;
    }

    public boolean hasKey(String key) {
        Boolean success = redisTemplate.hasKey(key);
        return success != null && success;
    }

    public boolean delKey(String... keys) {
        Long result = redisTemplate.delete(Arrays.asList(keys));
        return result != null && result.intValue() == 1;
    }


    // ===================== hash =====================

    public void hSet(String key, Map<String, String> val) {
        redisTemplate.<String, String>opsForHash().putAll(key, val);
    }

    public String hGet(String key, String hKey) {
        return redisTemplate.<String, String>opsForHash().get(key, hKey);
    }

    public Map<String, String> hGetAll(String key) {
        return redisTemplate.<String, String>opsForHash().entries(key);
    }

    // ===================== list =====================

    public void lPush(String key, String val) {
        redisTemplate.opsForList().leftPush(key, val);
    }

    public String rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public void rPush(String key, String val) {
        redisTemplate.opsForList().rightPush(key, val);
    }

    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public List<String> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    // ===================== set =====================

    public void sAdd(String key, String val) {
        redisTemplate.opsForSet().add(key, val);
    }

    public Set<String> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // ===================== sorted set =====================

    public void zAdd(String key, String val, double score) {
        redisTemplate.opsForZSet().add(key, val, score);
    }

    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<String>> zRevRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

}
