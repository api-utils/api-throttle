package com.nobodyhub.transcendence.api.throttle.policy.service;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.utils.ThrottlePolicyBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThrottlePolicyServiceTest {
    private ThrottlePolicyService service;

    @Before
    public void setUp() {
        this.service = new ThrottlePolicyService(null);
    }

    @Test
    public void checkTest() {
        BucketStatus status = new BucketStatus("10", "0", 5L);
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("bucket").nToken(10L).build();
        // check token pass
        assertTrue(this.service.check(policy, 0L, status));

        status = new BucketStatus("0", "0", 5L);
        policy = ThrottlePolicyBuilder.of("bucket").nToken(10L).build();
        // check token fail
        assertFalse(this.service.check(policy, 0L, status));

        status = new BucketStatus("10", "0", 5L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "10").nToken("10").interval("3").build();
        // check interval fail
        assertFalse(this.service.check(policy, 3000L, status));
        // check interval pass
        assertTrue(this.service.check(policy, 4000L, status));

        status = new BucketStatus("10", "0", 15L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "1").nToken("10").interval("3").build();
        // check window fails
        assertFalse(this.service.check(policy, 4000L, status));

        status = new BucketStatus("10", "0", 10L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "15").nToken("10").interval("3").build();
        // check window pass
        assertTrue(this.service.check(policy, 4000L, status));
    }

}