package com.nobodyhub.transcendence.api.throttle.policy.domain;

import lombok.AllArgsConstructor;

import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.getNonNegative;
import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.parseLong;

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

    public ThrottlePolicyBuilder window(String windowSize, String windowLimit) {
        Long wSize = getNonNegative(parseLong(windowSize));
        Long wLimit = getNonNegative(parseLong(windowLimit));
        if (wSize != null && wSize > 0
                && wLimit != null && wLimit > 0) {
            this.window(new BucketWindow(wSize, wLimit));
        }
        return this;
    }

    public ThrottlePolicyBuilder nToken(Long nToken) {
        this.policy.setNToken(getNonNegative(nToken));
        return this;
    }

    public ThrottlePolicyBuilder nToken(String nToken) {
        this.nToken(parseLong(nToken));
        return this;
    }

    public ThrottlePolicyBuilder interval(Long interval) {
        this.policy.setInterval(getNonNegative(interval));
        return this;
    }

    public ThrottlePolicyBuilder interval(String interval) {
        this.interval(parseLong(interval));
        return this;
    }

    public ThrottlePolicy build() {
        return this.policy;
    }
}
