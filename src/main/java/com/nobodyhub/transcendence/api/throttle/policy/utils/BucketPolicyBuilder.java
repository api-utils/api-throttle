package com.nobodyhub.transcendence.api.throttle.policy.utils;

import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketPolicy;
import com.nobodyhub.transcendence.api.throttle.policy.domain.BucketWindow;
import lombok.AllArgsConstructor;

import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.getNonNegative;
import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.parseLong;

@AllArgsConstructor
public final class BucketPolicyBuilder {
    private BucketPolicy policy;

    public static BucketPolicyBuilder of(String bucket) {
        BucketPolicy policy = new BucketPolicy();
        policy.setBucket(bucket);
        return new BucketPolicyBuilder(policy);
    }

    public BucketPolicyBuilder window(BucketWindow window) {
        this.policy.setWindow(window);
        return this;
    }

    public BucketPolicyBuilder window(String windowSize, String windowLimit) {
        Long wSize = getNonNegative(parseLong(windowSize));
        Long wLimit = getNonNegative(parseLong(windowLimit));
        if (wSize != null && wSize > 0
                && wLimit != null && wLimit > 0) {
            this.window(new BucketWindow(wSize, wLimit));
        }
        return this;
    }

    public BucketPolicyBuilder nToken(Long nToken) {
        this.policy.setNToken(getNonNegative(nToken));
        return this;
    }

    public BucketPolicyBuilder nToken(String nToken) {
        this.nToken(parseLong(nToken));
        return this;
    }

    public BucketPolicyBuilder interval(Long interval) {
        this.policy.setInterval(getNonNegative(interval));
        return this;
    }

    public BucketPolicyBuilder interval(String interval) {
        this.interval(parseLong(interval));
        return this;
    }

    public BucketPolicy build() {
        return this.policy;
    }
}
