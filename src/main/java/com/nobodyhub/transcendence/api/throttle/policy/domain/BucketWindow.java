package com.nobodyhub.transcendence.api.throttle.policy.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Scroll window restriction for bucket policy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BucketWindow {
    /**
     * window size in second
     */
    private long size;
    /**
     * maximum number of tokens allowed in window
     */
    private long limit;
}
