package com.nobodyhub.transcendence.api.throttle.bucket.utils;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketStatusBuilder {
    private BucketStatus status;

    public static BucketStatusBuilder of(String bucket) {
        BucketStatus status = new BucketStatus();
        status.setBucket(bucket);
        return new BucketStatusBuilder(status);
    }

    public static BucketStatusBuilder of(BucketStatus status) {
        return new BucketStatusBuilder(status);
    }

    public static BucketStatus of(ThrottlePolicy policy) {
        BucketStatusBuilder builder = BucketStatusBuilder.of(policy.getBucket());
        if (policy.getNToken() != null) {
            builder.nToken(policy.getNToken());
        }
        return builder.build();
    }

    public BucketStatusBuilder nToken(long nToken) {
        this.status.setNToken(nToken);
        return this;
    }

    public BucketStatusBuilder lastRequest(long lastRequest) {
        this.status.setLastRequest(lastRequest);
        return this;
    }

    public BucketStatusBuilder nWindowed(long nWindowed) {
        this.status.setNToken(nWindowed);
        return this;
    }

    public BucketStatus build() {
        return this.status;
    }
}
