package com.configcenter.common;

import com.configcenter.common.util.RedisOperation;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mjd on 2020/4/15 20:43
 */
public class RedisTest {

    private static RedisOperation redisOperation;

    @BeforeClass
    public static void setUp() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.afterPropertiesSet();
        redisOperation = new RedisOperation(redisTemplate);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        redisOperation.setRedisTemplate(redisTemplate);
    }

    private String key = "str_key";
    private String val = "str_val";

    @Test
    public void testSet() {
        redisOperation.set(key, val);
    }

    @Test
    public void testExpire() {
        redisOperation.setExpire(key, 10);
    }

    @Test
    public void get() {
        String value = redisOperation.get(key);
        assert (val.equals(value));
    }

    @Test
    public void hashSet() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        redisOperation.hSet("h_str_key", map);
    }

    @Test
    public void hashGet() {
        String o = redisOperation.hGet("h_str_key", "a");
        assert (o.equals("1"));
    }

    @Test
    public void hashGetAll() {
        Map<String, String> map = redisOperation.hGetAll("h_str_key");
        System.out.println(map);
    }

    @Test
    public void lPushAndRange() {
        redisOperation.lPush("queue", "a");
        redisOperation.lPush("queue", "b");
        redisOperation.lPush("queue", "c");
        List<String> filo = redisOperation.lRange("queue", 0, 2);
        System.out.println(filo);
    }

    @Test
    public void sAddAndGet() {
        redisOperation.sAdd("set", "a");
        redisOperation.sAdd("set", "a");
        redisOperation.sAdd("set", "b");
        redisOperation.sAdd("set", "c");
        Set<String> set = redisOperation.sGet("set");
        System.out.println(set);
    }

    @Test
    public void zAddAndGet() {
        redisOperation.zAdd("sorted_set", "abc", 100);
        redisOperation.zAdd("sorted_set", "edf", 101);
        redisOperation.zAdd("sorted_set", "ghi", 102);
        redisOperation.zAdd("sorted_set", "jkl", 103);
        Set<ZSetOperations.TypedTuple<String>> tuples = redisOperation.zRangeWithScores("sorted_set", 0, -1);
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            System.out.println("value:" + tuple.getValue() + ", score:" + tuple.getScore());
        }
        System.out.println("==== reverse order ====");
        Set<ZSetOperations.TypedTuple<String>> tuples2 = redisOperation.zRevRangeWithScores("sorted_set", 0, -1);
        for (ZSetOperations.TypedTuple<String> tuple : tuples2) {
            System.out.println("value:" + tuple.getValue() + ", score:" + tuple.getScore());
        }
    }

}
