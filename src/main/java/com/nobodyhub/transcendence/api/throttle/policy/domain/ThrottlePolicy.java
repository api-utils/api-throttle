package com.nobodyhub.transcendence.api.throttle.policy.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Policy that applied to each bucket
 */
@Document
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class ThrottlePolicy implements Comparable<ThrottlePolicy> {
    /**
     * name of bucket to which the policy applies
     */
    @Id
    private String bucket;
    /**
     * The window size in <b>second</b> to be measured. non-zero, positive
     * null value means infinite window size will be applied
     */
    private BucketWindow window;
    /**
     * Number of initial tokens
     * null value means no limitation on the number of the tokens
     */
    private long nToken;
    /**
     * Minimum interval in <b>millisecond</b> between executions, non-negative
     * null value means no interval required between executions
     */
    private long interval;

    /**
     * Policies should be sorted by name
     */
    @Override
    public int compareTo(ThrottlePolicy o) {
        return this.getBucket().compareTo(o.getBucket());
    }
}
