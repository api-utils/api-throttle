package com.nobodyhub.transcendence.api.throttle.policy.utils;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.bucket.utils.BucketStatusBuilder;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import org.junit.Test;

import static com.nobodyhub.transcendence.api.throttle.policy.utils.ThrottlePolicyUtil.check;
import static org.junit.Assert.*;

public class ThrottlePolicyUtilTest {
    @Test
    public void checkTest() {
        BucketStatus status = BucketStatusBuilder.of("bucketStatus").nToken(10L).lastRequest(0L).nWindowed(5L).build();
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("bucket").nToken(10L).build();
        // check token pass
        assertTrue(check(policy, 0L, status));

        status = BucketStatusBuilder.of("bucketStatus").nToken(0L).lastRequest(0L).nWindowed(5L).build();
        policy = ThrottlePolicyBuilder.of("bucket").nToken(10L).build();
        // check token fail
        assertFalse(check(policy, 0L, status));

        status = BucketStatusBuilder.of("bucketStatus").nToken(10L).lastRequest(0L).nWindowed(5L).build();
        policy = ThrottlePolicyBuilder.of("bucket").window(60L, 10L).nToken(10L).interval(3000L).build();
        // check interval fail
        assertFalse(check(policy, 2000L, status));
        // check interval pass
        assertTrue(check(policy, 3000L, status));
        assertTrue(check(policy, 4000L, status));

        status = BucketStatusBuilder.of("bucketStatus").nToken(10L).lastRequest(0L).nWindowed(15L).build();
        policy = ThrottlePolicyBuilder.of("bucket").window(60L, 1L).nToken(10L).interval(3000L).build();
        // check window fails
        assertFalse(check(policy, 4000L, status));

        status = BucketStatusBuilder.of("bucketStatus").nToken(10L).lastRequest(0L).nWindowed(10L).build();
        policy = ThrottlePolicyBuilder.of("bucket").window(60L, 15L).nToken(10L).interval(3000L).build();
        // check window pass
        assertTrue(check(policy, 4000L, status));
    }

    @Test
    public void getWindowLowerLimitTest() {
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("getWindowLowerLimitTest")
                .build();

        long actual = ThrottlePolicyUtil.getWindowLowerLimit(policy, 20000);
        // no window is specified
        assertEquals(20000, actual);

        policy.setWindow(new BucketWindow(10000, 5));

        actual = ThrottlePolicyUtil.getWindowLowerLimit(policy, 20000);
        // the earliest is after era
        assertEquals(10000, actual);

        actual = ThrottlePolicyUtil.getWindowLowerLimit(policy, 8000);
        // the earliest is before era
        assertEquals(0, actual);
    }
}