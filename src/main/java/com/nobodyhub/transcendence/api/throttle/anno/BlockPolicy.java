package com.nobodyhub.transcendence.api.throttle.anno;

/**
 * Policy to follow when the execution is blocked
 */
public enum BlockPolicy {
    /**
     * Wait until there is slot to execute
     */
    WAIT,
    /**
     * Skip current one and try to process next
     */
    SKIP,
    ;
}
