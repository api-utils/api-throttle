package com.nobodyhub.transcendence.api.throttle.bucket.repositiry;

import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;


@Slf4j
@Test
@SpringBootTest
@ContextConfiguration(classes = ThrottleBucketRepositoryTestConfiugration.class)
public class ThrottleBucketRepositoryTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private Boolean createBucketRst = Boolean.TRUE;


    @Autowired
    private ThrottleBucketRepository bucketRepository;

    @Test(threadPoolSize = 50, invocationCount = 50, timeOut = 10000)
    public void createBucketTest() {
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("createBucketTest")
                .nToken(10L)
                .build();
        log.info("Accessing createBucketRst {}.", createBucketRst.hashCode());
        createBucketRst = createBucketRst && this.bucketRepository.createBucket(policy);
        assertTrue(createBucketRst);
    }
}