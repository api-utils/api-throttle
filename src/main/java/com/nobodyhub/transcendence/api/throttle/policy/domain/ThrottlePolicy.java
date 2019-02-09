package com.nobodyhub.transcendence.api.throttle.policy.domain;

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
@NoArgsConstructor
@EqualsAndHashCode
public class ThrottlePolicy {
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
    private Long nToken;
    /**
     * Minimum interval in <b>second</b> between executions, non-negative
     * null value means no interval required between executions
     */
    private Long interval;
}
