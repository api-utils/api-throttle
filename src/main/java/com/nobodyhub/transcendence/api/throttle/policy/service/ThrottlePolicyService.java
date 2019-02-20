package com.nobodyhub.transcendence.api.throttle.policy.service;

import com.google.common.collect.Lists;
import com.nobodyhub.transcendence.api.throttle.api.domain.PagingResponse;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.repository.ThrottlePolicyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ThrottlePolicyService {
    private final ThrottlePolicyRepository policyRepository;

    @Cacheable(value = "throttle-policy", keyGenerator = "pageableKeyGenerator")
    public PagingResponse<ThrottlePolicy> findAll(Pageable pageable) {
        return PagingResponse.of(policyRepository.findAll(pageable));
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
        Optional<ThrottlePolicy> policy = this.policyRepository.findByBucket(bucket);
        return policy.orElse(null);
    }

    /**
     * Get policies for given bucket list
     *
     * @param buckets
     * @return
     */
    @NonNull
    public List<ThrottlePolicy> find(@NonNull List<String> buckets) {
        List<ThrottlePolicy> policies = Lists.newArrayList();
        for (String bucket : buckets) {
            ThrottlePolicy policy = find(bucket);
            if (policy != null) {
                policies.add(policy);
            }
        }
        return policies;
    }

    /**
     * Create/Update policy
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
}
