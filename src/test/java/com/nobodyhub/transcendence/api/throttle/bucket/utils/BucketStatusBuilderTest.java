package com.nobodyhub.transcendence.api.throttle.bucket.utils;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BucketStatusBuilderTest {
    @Test
    public void ofBucketTest() {
        BucketStatus status = BucketStatusBuilder.of("ofBucketTest")
                .nToken(100L)
                .lastRequest(200L)
                .nWindowed(300L)
                .build();
        assertNotNull(status);
        assertEquals("ofBucketTest", status.getBucket());
        assertEquals(100L, status.getNToken());
        assertEquals(200L, status.getLastRequest());
        assertEquals(300L, status.getNWindowed());
    }

    @Test
    public void ofPolicyTest() {
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("ofPolicyTest")
                .nToken(100L)
                .interval(1000L)
                .window(new BucketWindow(1000L, 10))
                .build();

        BucketStatus status = BucketStatusBuilder.of(policy).build();
        assertNotNull(status);
        assertEquals("ofPolicyTest", status.getBucket());
        assertEquals(100L, status.getNToken());
        assertEquals(0L, status.getLastRequest());
        assertEquals(0L, status.getNWindowed());
    }

    @Test
    public void decreaseNTokenTest() {
        BucketStatus status = BucketStatusBuilder.of("ofStatusTest")
                .nToken(100L)
                .lastRequest(200L)
                .nWindowed(300L)
                .decreaseNToken()
                .build();
        assertNotNull(status);
        assertEquals("ofStatusTest", status.getBucket());
        assertEquals(99L, status.getNToken());
        assertEquals(200L, status.getLastRequest());
        assertEquals(300L, status.getNWindowed());
    }
}