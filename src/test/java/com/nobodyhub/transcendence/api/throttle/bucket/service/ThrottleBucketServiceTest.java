package com.nobodyhub.transcendence.api.throttle.bucket.service;

import com.nobodyhub.transcendence.api.throttle.policy.service.ThrottlePolicyService;
import com.nobodyhub.transcendence.api.throttle.policy.utils.ThrottlePolicyBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ThrottleBucketServiceTestConfiguration.class)
public class ThrottleBucketServiceTest {
    @Autowired
    private ThrottleBucketService bucketService;

    @Autowired
    private ThrottlePolicyService policyService;

    @Test
    public void createBucketTest() {
        policyService.update(ThrottlePolicyBuilder.of("createBucketTest").nToken(10L).build());

        bucketService.createBucket("createBucketTest");
        String execToken = bucketService.updateBucket("createBucketTest");
        assertNotNull(execToken);
        assertTrue(bucketService.checkExecToken("createBucketTest", execToken));

    }
}