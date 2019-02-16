package com.nobodyhub.transcendence.api.throttle.policy.domain;


import lombok.*;

/**
 * Scroll window restriction for bucket policy
 */
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE) // used by json
@AllArgsConstructor
@EqualsAndHashCode
public class BucketWindow {
    /**
     * window size in milliseconds
     */
    private long size;
    /**
     * maximum number of tokens allowed in window
     */
    private long limit;
}
