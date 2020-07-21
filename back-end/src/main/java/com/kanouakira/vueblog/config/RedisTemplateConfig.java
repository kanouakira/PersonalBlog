package com.kanouakira.vueblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {
    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplate(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisTemplate.getDefaultSerializer());
        return redisTemplate;
    }
}
