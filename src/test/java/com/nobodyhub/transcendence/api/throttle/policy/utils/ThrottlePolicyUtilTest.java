package com.nobodyhub.transcendence.api.throttle.policy.utils;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import org.junit.Test;

import static com.nobodyhub.transcendence.api.throttle.policy.utils.ThrottlePolicyUtil.check;
import static org.junit.Assert.*;

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
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "10").nToken("10").interval("3000").build();
        // check interval fail
        assertFalse(check(policy, 3000L, status));
        // check interval pass
        assertTrue(check(policy, 4000L, status));

        status = new BucketStatus("10", "0", 15L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "1").nToken("10").interval("3000").build();
        // check window fails
        assertFalse(check(policy, 4000L, status));

        status = new BucketStatus("10", "0", 10L);
        policy = ThrottlePolicyBuilder.of("bucket").window("60", "15").nToken("10").interval("3000").build();
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