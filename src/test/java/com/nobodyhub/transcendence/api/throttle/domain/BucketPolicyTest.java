package com.nobodyhub.transcendence.api.throttle.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class BucketPolicyTest {
    @Test
    public void allArgConstructorTest() {
        BucketPolicy policy = new BucketPolicy("60", "10", "100", "3");
        assertEquals(60L, policy.getWindow().getSize());
        assertEquals(10L, policy.getWindow().getLimit());
        assertEquals((Long) 100L, policy.getNToken());
        assertEquals((Long) 3L, policy.getInterval());

        policy = new BucketPolicy("size", "limit", "-1", null);
        assertNull(policy.getWindow());
        assertEquals((Long) 0L, policy.getNToken());
        assertNull(policy.getInterval());
    }

    @Test
    public void constructorTokenTest() {
        BucketPolicy policy = new BucketPolicy(10L);
        assertNull(policy.getWindow());
        assertEquals((Long) 10L, policy.getNToken());
        assertNull(policy.getInterval());

        policy = new BucketPolicy(-1L);
        assertNull(policy.getWindow());
        assertEquals((Long) 0L, policy.getNToken());
        assertNull(policy.getInterval());
    }

    @Test
    public void checkTest() {
        BucketStatus status = new BucketStatus("10", "0", 5L);
        BucketPolicy policy = new BucketPolicy(10L);
        // check token pass
        assertTrue(policy.check(0L, status));

        status = new BucketStatus("0", "0", 5L);
        policy = new BucketPolicy(10L);
        // check token fail
        assertFalse(policy.check(0L, status));


        status = new BucketStatus("10", "0", 5L);
        policy = new BucketPolicy("60", "10", "10", "3");
        // check interval fail
        assertFalse(policy.check(3000L, status));
        // check interval pass
        assertTrue(policy.check(4000L, status));

        status = new BucketStatus("10", "0", 15L);
        policy = new BucketPolicy("60", "1", "10", "3");
        // check window fails
        assertFalse(policy.check(4000L, status));

        status = new BucketStatus("10", "0", 10L);
        policy = new BucketPolicy("60", "15", "10", "3");
        // check window pass
        assertTrue(policy.check(4000L, status));
    }
}