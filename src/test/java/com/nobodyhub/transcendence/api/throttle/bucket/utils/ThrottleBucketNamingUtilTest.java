package com.nobodyhub.transcendence.api.throttle.bucket.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ThrottleBucketNamingUtilTest {
    @Test
    public void statusTest() {
        assertEquals("statusTest:status",
                ThrottleBucketNamingUtil.status("statusTest")
        );
    }

    @Test
    public void windowTest() {
        assertEquals("windowTest:window",
                ThrottleBucketNamingUtil.window("windowTest")
        );
    }

}