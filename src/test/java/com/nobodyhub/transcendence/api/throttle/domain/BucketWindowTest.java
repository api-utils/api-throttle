package com.nobodyhub.transcendence.api.throttle.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class BucketWindowTest {
    @Test
    public void getEarliestTest() {
        BucketWindow window = new BucketWindow(100L, 10L);
        assertEquals(0L, window.getEarliest(90L));
        assertEquals(0L, window.getEarliest(100L));
        assertEquals(10L, window.getEarliest(110L));
    }

    @Test
    public void checkTest() {
        BucketWindow window = new BucketWindow(100L, 10L);
        assertTrue(window.check(5L));
        assertTrue(window.check(10L));
        assertFalse(window.check(15L));
    }
}