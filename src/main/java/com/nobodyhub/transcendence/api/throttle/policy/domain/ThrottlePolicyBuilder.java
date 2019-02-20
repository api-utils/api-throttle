package com.nobodyhub.transcendence.api.throttle.policy.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ThrottlePolicyBuilder {
    private ThrottlePolicy policy;

    public static ThrottlePolicyBuilder of(String bucket) {
        ThrottlePolicy policy = new ThrottlePolicy();
        policy.setBucket(bucket);
        return new ThrottlePolicyBuilder(policy);
    }

    public ThrottlePolicyBuilder window(BucketWindow window) {
        this.policy.setWindow(window);
        return this;
    }

    public ThrottlePolicyBuilder window(long wSize, long wLimit) {
        if (wSize > 0 && wLimit > 0) {
            this.window(new BucketWindow(wSize, wLimit));
        }
        return this;
    }

    public ThrottlePolicyBuilder nToken(Long nToken) {
        this.policy.setNToken(nToken < 0 ? 0 : nToken);
        return this;
    }

    public ThrottlePolicyBuilder interval(Long interval) {
        this.policy.setInterval(interval < 0 ? 0 : interval);
        return this;
    }

    public ThrottlePolicy build() {
        return this.policy;
    }
}
