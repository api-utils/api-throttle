package com.nobodyhub.transcendence.api.throttle.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BucketStatusTest {
    @Test
    public void allArgConstructorTest() {
        BucketStatus status = new BucketStatus("token", "lastRequest", null);
        assertNull(status.getNToken());
        assertNull(status.getLastRequest());
        assertNull(status.getNWindowed());

        status = new BucketStatus("123", "3000", 100L);
        assertEquals((Long) 123L, status.getNToken());
        assertEquals((Long) 3000L, status.getLastRequest());
        assertEquals((Long) 100L, status.getNWindowed());

        status = new BucketStatus("-123", "-3000", -100L);
        assertEquals((Long) 0L, status.getNToken());
        assertEquals((Long) 0L, status.getLastRequest());
        assertEquals((Long) 0L, status.getNWindowed());
    }
}