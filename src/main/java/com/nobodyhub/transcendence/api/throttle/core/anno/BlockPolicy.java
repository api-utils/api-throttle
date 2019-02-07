package com.nobodyhub.transcendence.api.throttle.core.anno;

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
