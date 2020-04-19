package com.configcenter.admin.config;

import com.configcenter.common.util.RedisOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by mjd on 2020/4/15 20:03
 */
@Configuration
public class RedisConfig {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public RedisOperation redisOperation() {
        return new RedisOperation(stringRedisTemplate);
    }

}
