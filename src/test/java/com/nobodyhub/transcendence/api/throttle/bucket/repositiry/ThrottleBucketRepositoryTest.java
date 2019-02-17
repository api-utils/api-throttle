package com.nobodyhub.transcendence.api.throttle.bucket.repositiry;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.nobodyhub.transcendence.api.throttle.bucket.utils.ThrottleBucketNamingUtil.status;
import static com.nobodyhub.transcendence.api.throttle.bucket.utils.ThrottleBucketNamingUtil.window;
import static org.testng.AssertJUnit.assertTrue;


@Slf4j
@Test
@SpringBootTest
@ContextConfiguration(classes = ThrottleBucketRepositoryTestConfiugration.class)
public class ThrottleBucketRepositoryTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ThrottleBucketRepository bucketRepository;

    private Boolean createBucketRst = Boolean.TRUE;
    private ThrottlePolicy policy;

    @BeforeClass
    public void createBucketTestSetup() {
        this.policy = ThrottlePolicyBuilder.of("createBucketTest")
                .nToken(10L)
                .build();
    }

    @Test(threadPoolSize = 50, invocationCount = 50, timeOut = 100000)
    public void createBucketTest() {
        log.info("Accessing createBucketRst {}.", createBucketRst.hashCode());
        createBucketRst = createBucketRst && this.bucketRepository.createBucket(this.policy);
        assertTrue(createBucketRst);
    }

    private AtomicInteger checkBucketRst = new AtomicInteger(0);
    private List<ThrottlePolicy> policies;

    @BeforeClass
    public void setUp() {
        this.policies = Lists.newArrayList();
        this.policies.add(ThrottlePolicyBuilder.of("checkBucketTest1").nToken(100L).build());
        this.policies.add(ThrottlePolicyBuilder.of("checkBucketTest2").nToken(10L).build());
    }

    @Test(threadPoolSize = 50, invocationCount = 100, timeOut = 100000)
    public void checkBucketTest() {
        boolean executed = bucketRepository.checkBucket(this.policies);
        if (executed) {
            log.info("checkBucketRst: {}", checkBucketRst.incrementAndGet());
        }
        assertTrue(checkBucketRst.get() <= 10);
    }

    @AfterClass
    public void tearDown() {
        clearBucket("createBucketTest");
        clearBucket("checkBucketTest1");
        clearBucket("checkBucketTest2");
    }

    private void clearBucket(String bucekt) {
        this.redisTemplate.delete(status(bucekt));
        this.redisTemplate.delete(window(bucekt));
    }
}