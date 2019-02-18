package com.nobodyhub.transcendence.api.throttle.policy.repository;

import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ThrottlePolicyRepository extends PagingAndSortingRepository<ThrottlePolicy, String> {
    /**
     * find policy by name
     *
     * @param bucket bucket name
     * @return policy with given bucket name
     */
    Optional<ThrottlePolicy> findByBucket(String bucket);
}
