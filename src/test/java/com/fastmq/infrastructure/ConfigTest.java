package com.fastmq.infrastructure;

import com.fastmq.BaseTest;
import org.junit.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;


public class ConfigTest extends BaseTest {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void fastMQJedisPoolTest() {
        System.out.println(redissonClient);
    }
}
