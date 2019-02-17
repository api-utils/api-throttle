package com.nobodyhub.transcendence.api.throttle.bucket.utils;

import com.nobodyhub.transcendence.api.throttle.bucket.domain.BucketStatus;
import com.nobodyhub.transcendence.api.throttle.policy.domain.ThrottlePolicy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * Builder/Updater for {@link BucketStatus}
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketStatusBuilder {
    private BucketStatus status;

    /**
     * create from bucket name
     *
     * @param bucket
     * @return
     */
    public static BucketStatusBuilder of(String bucket) {
        BucketStatus status = new BucketStatus();
        status.setBucket(bucket);
        return of(status);
    }

    /**
     * Update given status
     *
     * @param status
     * @return
     */
    public static BucketStatusBuilder of(BucketStatus status) {
        return new BucketStatusBuilder(status);
    }

    /**
     * Create from policy
     *
     * @param policy
     * @return
     */
    public static BucketStatusBuilder of(ThrottlePolicy policy) {
        BucketStatusBuilder builder = BucketStatusBuilder.of(policy.getBucket());
        if (policy.getNToken() != null) {
            builder = builder.nToken(policy.getNToken());
        }
        return builder;
    }

    /**
     * set bucket tokens
     *
     * @return
     */
    public BucketStatusBuilder nToken(long nToken) {
        this.status.setNToken(nToken);
        return this;
    }

    /**
     * descrease the token in the bucket
     *
     * @return
     */
    public BucketStatusBuilder decreaseNToken() {
        this.status.setNToken(this.status.getNToken() - 1);
        return this;
    }

    /**
     * @param lastRequest
     * @return
     */
    public BucketStatusBuilder lastRequest(long lastRequest) {
        this.status.setLastRequest(lastRequest);
        return this;
    }

    /**
     * Set the number of execution in the window
     *
     * @param nWindowed
     * @return
     */
    public BucketStatusBuilder nWindowed(long nWindowed) {
        this.status.setNWindowed(nWindowed);
        return this;
    }

    /**
     * @return the final bucket status built
     */
    @NonNull
    public BucketStatus build() {
        return this.status;
    }
}
