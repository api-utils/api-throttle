package com.nobodyhub.transcendence.api.throttle.policy.service;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.api.domain.PagingResponse;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ThrottlePolicyServiceTestConfiguration.class)
public class ThrottlePolicyServiceSpringTest {
    @Autowired
    private ThrottlePolicyService policyService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisCacheTest() {
        policyService.delete("findTest");
        ThrottlePolicy found = policyService.find("findTest");
        assertNull(found);
        // get from cache
        assertNull(found);
        assertTrue(policyService.find(Lists.newArrayList("findTest")).isEmpty());

        ThrottlePolicy policy = ThrottlePolicyBuilder.of("findTest")
                .nToken(100L)
                .window(new BucketWindow(60, 100))
                .interval(3L)
                .build();

        ThrottlePolicy updated = policyService.update(policy);
        assertEquals(policy.getNToken(), updated.getNToken());
        assertEquals(policy.getWindow(), updated.getWindow());
        assertEquals(policy.getInterval(), updated.getInterval());
        assertEquals(policy.getBucket(), updated.getBucket());

        assertNotNull(policyService.find(Lists.newArrayList("findTest")));
        assertEquals(1, policyService.find(Lists.newArrayList("findTest")).size());

        policyService.delete("findTest");
        found = policyService.find("findTest");
        assertNull(found);
    }

    @Test
    public void redisCacheTest2() {
        PageRequest pageable = PageRequest.of(3, 20);
        PagingResponse<ThrottlePolicy> page = this.policyService.findAll(pageable);
        assertNotNull(page);
        redisTemplate.delete("throttle-policy::Page request [number: 3, size 20, sort: UNSORTED]");
    }
}