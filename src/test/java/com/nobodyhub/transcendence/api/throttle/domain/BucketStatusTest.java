package com.nobodyhub.transcendence.api.throttle.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BucketStatusTest {
    @Test
    public void allArgConstructorTest() {
        BucketStatus status = new BucketStatus("token", "lastRequest", null);
        assertEquals(0, status.getNToken());
        assertEquals(0, status.getLastRequest());
        assertEquals(0, status.getNWindowed());

        status = new BucketStatus("123", "3000", 100L);
        assertEquals(123L, status.getNToken());
        assertEquals(3000L, status.getLastRequest());
        assertEquals(100L, status.getNWindowed());

        status = new BucketStatus("-123", "-3000", -100L);
        assertEquals(0L, status.getNToken());
        assertEquals(0L, status.getLastRequest());
        assertEquals(0L, status.getNWindowed());
    }
}