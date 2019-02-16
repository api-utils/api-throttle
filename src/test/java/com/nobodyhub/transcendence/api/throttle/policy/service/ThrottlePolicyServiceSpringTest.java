package com.nobodyhub.transcendence.api.throttle.policy.service;

import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ThrottlePolicyServiceTestConfiguration.class)
public class ThrottlePolicyServiceSpringTest {
    @Autowired
    private ThrottlePolicyService policyService;

    @Test
    public void redisCacheTest() {
        ThrottlePolicy found = policyService.find("findTest");
        assertNull(found);

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

        policyService.delete("findTest");
        found = policyService.find("findTest");
        assertNull(found);
    }
}