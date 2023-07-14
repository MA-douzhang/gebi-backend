package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisLimiterManagerTest {

    @Resource
    RedisLimiterManager redisLimiterManager;

    @Test
    void doRateLimit() {
        long userId = 1L;
        for (int i = 0; i < 1; i++) {
            redisLimiterManager.doRateLimit("doRateLimit_" + userId);
            System.out.println("success");
        }
    }
}
