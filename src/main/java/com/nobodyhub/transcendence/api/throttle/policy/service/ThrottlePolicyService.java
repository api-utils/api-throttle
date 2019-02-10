package com.nobodyhub.transcendence.api.throttle.policy.service;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.repository.ThrottlePolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ThrottlePolicyService {
    private final ThrottlePolicyRepository policyRepository;

    protected ThrottlePolicyService(ThrottlePolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public Page<ThrottlePolicy> findAll(Pageable pageable) {
        return policyRepository.findAll(pageable);
    }

    /**
     * Get policy by bucket name
     *
     * @param bucket
     * @return
     */
    @Cacheable(value = "throttle-policy", key = "#bucket")
    @Nullable
    public ThrottlePolicy find(@NonNull String bucket) {
        Optional<ThrottlePolicy> policy = this.policyRepository.getByBucket(bucket);
        return policy.orElse(null);
    }

    /**
     * Update policy
     *
     * @param policy
     * @return
     */
    @CachePut(value = "throttle-policy", key = "#policy.bucket")
    @NonNull
    public ThrottlePolicy update(@NonNull ThrottlePolicy policy) {
        return this.policyRepository.save(policy);
    }

    /**
     * Delete policy
     *
     * @param bucket
     */
    @CacheEvict(value = "throttle-policy", key = "#bucket")
    public void delete(@NonNull String bucket) {
        this.policyRepository.deleteById(bucket);
    }

    /**
     * check whether can proceed to execute with given status
     *
     * @param policy    policy to check
     * @param timestamp timestamp when execute
     * @param status    current status
     * @return true to proceed the execution
     */
    public boolean check(ThrottlePolicy policy, long timestamp, BucketStatus status) {
        boolean checkWindow = policy.getWindow() == null || check(policy.getWindow(), status.getNWindowed());
        boolean checkToken = status.getNToken() > 0;
        boolean checkInterval = policy.getInterval() == null || timestamp - status.getLastRequest() > policy.getInterval() * 1000;
        return checkInterval && checkWindow && checkToken;
    }

    /**
     * @param policy
     * @param timestamp
     * @return
     */
    public long getWindowUpperLimit(ThrottlePolicy policy, long timestamp) {
        if (policy.getWindow() != null) {
            return getEarliest(policy.getWindow(), timestamp);
        }
        return timestamp;
    }

    /**
     * Get the earliest timestamp that should be retained in the window
     *
     * @param timestamp
     * @return
     */
    private long getEarliest(BucketWindow window, long timestamp) {
        long earliest = timestamp - window.getSize();
        return earliest > 0 ? earliest : 0;
    }

    /**
     * Check whether number of executions in the window is within the limit or not
     *
     * @param nWindowed number of executions in the window
     * @return
     */
    private boolean check(BucketWindow window, long nWindowed) {
        return nWindowed < window.getLimit();
    }
}
