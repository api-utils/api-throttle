package com.nobodyhub.transcendence.api.throttle.bucket.service;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.bucket.repositiry.ThrottleBucketRepository;
import com.nobodyhub.transcendence.api.throttle.bucket.utils.BucketStatusBuilder;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicyBuilder;
import com.nobodyhub.transcendence.api.throttle.policy.service.ThrottlePolicyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ThrottleBucketServiceTestConfiguration.class)
public class ThrottleBucketServiceTest {
    @Autowired
    private ThrottleBucketService bucketService;

    @MockBean
    private ThrottlePolicyService policyService;

    @MockBean
    private ThrottleBucketRepository bucketRepository;

    @Test
    public void findBuckets() {
        List<String> buckets = Lists.newArrayList(
                "findBucket1",
                "findBucket2",
                "findBucket3");
        BucketStatus s1 = BucketStatusBuilder.of("findBucket1").lastRequest(100L).build();
        BucketStatus s2 = BucketStatusBuilder.of("findBucket2").nToken(10L).build();
        BucketStatus s3 = BucketStatusBuilder.of("findBucket3").nWindowed(20L).build();
        List<BucketStatus> statuses = Lists.newArrayList(s1, s2, s3);
        given(bucketRepository.getBucketStatus(buckets))
                .willReturn(statuses);
        List<BucketStatus> actuals = this.bucketService.findBucket(buckets);
        assertEquals(3, actuals.size());
        assertEquals(s1, actuals.get(0));
        assertEquals(s2, actuals.get(1));
        assertEquals(s3, actuals.get(2));
    }

    @Test
    public void findBucket() {
        BucketStatus status = BucketStatusBuilder.of("findBucket")
                .nWindowed(10L)
                .lastRequest(100L)
                .nToken(20L).build();
        given(bucketRepository.getBucketStatus("findBucket"))
                .willReturn(status);
        BucketStatus actual = this.bucketService.findBucket("findBucket");
        assertEquals(status, actual);
    }

    @Test
    public void createBucketTest() {
        ThrottlePolicy policy = ThrottlePolicyBuilder.of("createBucketTest").nToken(10L).build();
        given(this.policyService.find("createBucketTest")).willReturn(policy);
        given(this.bucketRepository.createBucket(policy)).willReturn(true);
        assertTrue(this.bucketService.createBucket("createBucketTest"));

        given(this.policyService.find("createBucketTest")).willReturn(null);
        assertFalse(this.bucketService.createBucket("createBucketTest"));
    }

    @Test
    public void checkBucket() {
        List<String> buckets = Lists.newArrayList(
                "checkBucket1", "checkBucket2", "checkBucket3"
        );
        ThrottlePolicy p1 = ThrottlePolicyBuilder.of("checkBucket1").build();
        ThrottlePolicy p2 = ThrottlePolicyBuilder.of("checkBucket2").build();
        ThrottlePolicy p3 = ThrottlePolicyBuilder.of("checkBucket3").build();

        List<ThrottlePolicy> policies = Lists.newArrayList(p1, p2, p3);
        given(this.policyService.find(buckets)).willReturn(policies);
        given(this.bucketRepository.checkBucket(policies)).willReturn(true);
        assertTrue(this.bucketService.checkBucket("checkBucket1", "checkBucket2", "checkBucket3"));

        policies = Lists.newArrayList(p1, p2);
        given(this.policyService.find(buckets)).willReturn(policies);
        assertFalse(this.bucketService.checkBucket("checkBucket1", "checkBucket2", "checkBucket3"));
    }
}