package com.nobodyhub.transcendence.api.throttle.policy.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Scroll window restriction for bucket policy
 */
@Data
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
