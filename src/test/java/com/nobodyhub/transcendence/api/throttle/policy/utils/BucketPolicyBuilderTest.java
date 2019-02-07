package com.nobodyhub.transcendence.api.throttle.policy.utils;

import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BucketPolicyBuilderTest {
    @Test
    public void test() {
        BucketPolicy policy = BucketPolicyBuilder.of("bucket")
                .window("60", "10")
                .nToken("100")
                .interval("3")
                .build();
        assertEquals("bucket", policy.getBucket());
        assertEquals(60L, policy.getWindow().getSize());
        assertEquals(10L, policy.getWindow().getLimit());
        assertEquals((Long) 100L, policy.getNToken());
        assertEquals((Long) 3L, policy.getInterval());

        policy = BucketPolicyBuilder.of("bucket")
                .window("size", "limit")
                .nToken("-1")
                .interval((String) null)
                .build();
        assertEquals("bucket", policy.getBucket());
        assertNull(policy.getWindow());
        assertEquals((Long) 0L, policy.getNToken());
        assertNull(policy.getInterval());

        policy = BucketPolicyBuilder.of("bucket")
                .nToken(10L)
                .build();
        assertEquals("bucket", policy.getBucket());
        assertNull(policy.getWindow());
        assertEquals((Long) 10L, policy.getNToken());
        assertNull(policy.getInterval());

        policy = BucketPolicyBuilder.of("bucket")
                .nToken(-1L)
                .build();
        assertEquals("bucket", policy.getBucket());
        assertNull(policy.getWindow());
        assertEquals((Long) 0L, policy.getNToken());
        assertNull(policy.getInterval());
    }

}