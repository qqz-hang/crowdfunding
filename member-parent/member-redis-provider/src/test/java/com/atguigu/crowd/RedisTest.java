package com.atguigu.crowd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    private Logger logger = LoggerFactory.getLogger(RedisTest.class);
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String key = "key11111";
        String value = "value111";
        valueOperations.set(key, value);
        String stringValue = valueOperations.get("key1");
        logger.info("stringValue"+stringValue);
    }

    @Test
    public void testExSet() {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String key = "apple";
        String value = "red";
        valueOperations.set(key, value, 5000, TimeUnit.SECONDS);
        String stringValue = valueOperations.get(value);
        logger.info(stringValue);
    }
}
