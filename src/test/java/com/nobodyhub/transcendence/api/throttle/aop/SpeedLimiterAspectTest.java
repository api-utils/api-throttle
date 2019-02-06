package com.nobodyhub.transcendence.api.throttle.aop;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.domain.BucketPolicy;
import com.nobodyhub.transcendence.api.throttle.service.TokenBucketService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {SpeedLimiterAspectTestConfiguration.class})
public class SpeedLimiterAspectTest {

    @Autowired
    private TokenBucketService tokenBucketService;

    @Autowired
    private SpeedLimiterAspectTestConfiguration.TestClass testClass;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SpeedLimiterAspect aspect;

    @Test
    public void test() {
        BucketPolicy policy = new BucketPolicy(13L);
        tokenBucketService.updateBucketPolicy("TestBucket", policy);

        List<Long> executions = Lists.newArrayList();

        int nTimes = 100;
        while (nTimes-- > 0) {
            testClass.execute(executions);
        }

        assertEquals(13, executions.size());
    }

    @After
    public void tearDown() {
        redisTemplate.delete("TestBucket_policy");
    }
}

