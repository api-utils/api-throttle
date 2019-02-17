package com.nobodyhub.transcendence.api.throttle.policy.controller;

import com.nobodyhub.transcendence.api.throttle.api.domain.PagingResponse;
import com.nobodyhub.transcendence.api.throttle.api.domain.SingleResponse;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import com.nobodyhub.transcendence.api.throttle.policy.service.ThrottlePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/throttle/policy")
@RequiredArgsConstructor
public class ThrottlePolicyController {
    private final ThrottlePolicyService policyService;

    /**
     * Find all policies
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(path = "/all")
    PagingResponse<ThrottlePolicy> findAllPolicies(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                   @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return PagingResponse.of(policyService.findAll(pageable));
    }

    /**
     * Find policy by bucket name
     *
     * @param bucket bucket name
     * @return
     */
    @GetMapping(path = "/bucket/{bucket}")
    SingleResponse<ThrottlePolicy> findByBucket(@PathVariable("bucket") String bucket) {
        ThrottlePolicy policy = policyService.find(bucket);
        return SingleResponse.of(policy);
    }
}
