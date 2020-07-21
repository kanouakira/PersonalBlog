package com.kanouakira.vueblog;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@SpringBootTest(classes = VueblogApplication.class)
@RunWith(SpringRunner.class)
class VueblogApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate redisTemplate;


    @Test
    void redisTest (){
//        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
//        stringStringValueOperations.set("name", "kanouakira");
//        System.out.println(stringStringValueOperations.get("name"));
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Set viewRank = zSetOperations.reverseRange("viewRank", 0, -1);
        System.out.println(viewRank);
//        keys.forEach(key -> {
//            System.out.println(key);
//            System.out.println(stringRedisTemplate.type(key));
//            System.out.println(stringRedisTemplate.opsForValue().get(key));
//            System.out.println("--------------------");
//        });
    }
}


