package com.nobodyhub.transcendence.api.throttle.bucket.service;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.bucket.repositiry.ThrottleBucketRepository;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.service.ThrottlePolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that controls the bucket status
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThrottleBucketService {
    private final ThrottleBucketRepository bucketRepository;
    private final ThrottlePolicyService policyService;

    @Nullable
    public List<BucketStatus> findBucket(@NonNull String... buckets) {
        return bucketRepository.getBucketStatus(buckets);
    }

    /**
     * Get bucket status by name
     *
     * @param bucket bucket status
     * @return
     */
    @Nullable
    public BucketStatus findBucket(@NonNull String bucket) {
        return bucketRepository.getBucketStatus(bucket);
    }

    /**
     * Create new bucket for the policy of given name
     *
     * @param bucket
     */
    public void createBucket(@NonNull String bucket) {
        ThrottlePolicy policy = policyService.find(bucket);
        if (policy != null) {
            bucketRepository.createBucket(policy);
            return;
        }
        log.warn("No policy found for Bucket: {}! No throttle will be applied!", bucket);
    }

    /**
     * Update bucket status
     *
     * @param bucket
     * @return execution token
     */
    @Nullable
    public String updateBucket(@NonNull String bucket) {
        ThrottlePolicy policy = policyService.find(bucket);
        if (policy != null) {
            return bucketRepository.updateBucket(policy);
        }
        return null;
    }

    /**
     * check whether to proceed with the execution token
     *
     * @param bucket    bucket name
     * @param execToken execution token
     * @return
     */
    public boolean checkExecToken(@NonNull String bucket,
                                  @NonNull String execToken) {
        return this.bucketRepository.checkExecToken(bucket, execToken);
    }
}
