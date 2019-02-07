package com.nobodyhub.transcendence.api.throttle.policy.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Scroll window restriction for bucket policy
 */
@Getter
@AllArgsConstructor
public class BucketWindow {
    /**
     * window size in second
     */
    private long size;
    /**
     * maximum number of tokens allowed in window
     */
    private long limit;

    /**
     * Get the earliest timestamp that should be retained in the window
     *
     * @param timestamp
     * @return
     */
    public long getEarliest(long timestamp) {
        long earliest = timestamp - this.size;
        return earliest > 0 ? earliest : 0;
    }

    /**
     * Check whether number of executions in the window is within the limite or not
     *
     * @param nWindowed number of executions in the window
     * @return
     */
    public boolean check(long nWindowed) {
        return nWindowed < limit;
    }
}
