package com.nobodyhub.transcendence.api.throttle.bucket.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ThrottleBucketServiceTestConfiguration.class)
public class ThrottleBucketServiceTest {
    @Autowired
    private ThrottleBucketService bucketService;

    @Test
    public void createBucketTest() {
        bucketService.createBucket("ThrottleBucketServiceTest");
    }
}