package com.nobodyhub.transcendence.api.throttle.policy.utils;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import org.junit.Test;

import static com.nobodyhub.transcendence.api.throttle.policy.utils.ThrottlePolicyUtil.check;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThrottlePolicyUtilTest {
    @Test
    public void checkTest() {
        BucketStatus status = new BucketStatus("10", "0", 5L);
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("bucket").nToken(10L).build();
        // check token pass
        assertTrue(check(policy, 0L, status));

        status = new BucketStatus("0", "0", 5L);
        policy = ThrottlePolicyBuilder.of("bucket").nToken(10L).build();
        // check token fail
        assertFalse(check(policy, 0L, status));

        status = new BucketStatus("10", "0", 5L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "10").nToken("10").interval("3").build();
        // check interval fail
        assertFalse(check(policy, 3000L, status));
        // check interval pass
        assertTrue(check(policy, 4000L, status));

        status = new BucketStatus("10", "0", 15L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "1").nToken("10").interval("3").build();
        // check window fails
        assertFalse(check(policy, 4000L, status));

        status = new BucketStatus("10", "0", 10L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "15").nToken("10").interval("3").build();
        // check window pass
        assertTrue(check(policy, 4000L, status));
    }
}