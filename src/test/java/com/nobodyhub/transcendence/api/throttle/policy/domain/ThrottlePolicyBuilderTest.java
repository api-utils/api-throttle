package com.nobodyhub.transcendence.api.throttle.policy.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ThrottlePolicyBuilderTest {
    @Test
    public void test() {
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("bucket")
                .window(60L, 10L)
                .nToken(100L)
                .interval(3L)
                .build();
        assertEquals("bucket", policy.getBucket());
        assertEquals(60L, policy.getWindow().getSize());
        assertEquals(10L, policy.getWindow().getLimit());
        assertEquals(100L, policy.getNToken());
        assertEquals(3L, policy.getInterval());

        policy = ThrottlePolicyBuilder.of("bucket")
                .window(-1L, -1L)
                .nToken(-1L)
                .interval(-1L)
                .build();
        assertEquals("bucket", policy.getBucket());
        assertNull(policy.getWindow());
        assertEquals(0L, policy.getNToken());
        assertEquals(0L, policy.getInterval());

        policy = ThrottlePolicyBuilder.of("bucket")
                .nToken(10L)
                .build();
        assertEquals("bucket", policy.getBucket());
        assertNull(policy.getWindow());
        assertEquals(10L, policy.getNToken());
        assertEquals(0L, policy.getInterval());

        policy = ThrottlePolicyBuilder.of("bucket")
                .nToken(-1L)
                .build();
        assertEquals("bucket", policy.getBucket());
        assertNull(policy.getWindow());
        assertEquals(0L, policy.getNToken());
        assertEquals(0L, policy.getInterval());
    }

}