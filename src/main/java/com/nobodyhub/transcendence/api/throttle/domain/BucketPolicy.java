package com.nobodyhub.transcendence.api.throttle.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.nobodyhub.transcendence.api.throttle.utils.NumberUtils.getNonNegative;
import static com.nobodyhub.transcendence.api.throttle.utils.NumberUtils.parseLong;

/**
 * Policy that applied to each bucket
 */
@Getter
@Slf4j
public class BucketPolicy {
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

    /**
     * @param windowSize  window size
     * @param windowLimit window token number
     * @param nToken      initial number of token
     * @param interval    interval between execution
     */
    public BucketPolicy(String windowSize, String windowLimit, String nToken, String interval) {
        Long wSize = getNonNegative(parseLong(windowSize));
        Long wLimit = getNonNegative(parseLong(windowLimit));
        if (wSize != null && wSize > 0
                && wLimit != null && wLimit > 0) {
            this.window = new BucketWindow(wSize, wLimit);
        }
        this.nToken = getNonNegative(parseLong(nToken));
        this.interval = getNonNegative(parseLong(interval));
    }

    public BucketPolicy(Long nToken) {
        this.nToken = getNonNegative(nToken);
    }

    /**
     * check whether can proceed to execute with given status
     *
     * @param timestamp timestamp when execute
     * @param status    current status
     * @return true to proceed the execution
     */
    public boolean check(long timestamp, BucketStatus status) {
        boolean checkWindow = window == null || window.check(status.getNWindowed());
        boolean checkToken = status.getNToken() > 0;
        boolean checkInterval = interval == null || timestamp - status.getLastRequest() > interval * 1000;
        return checkInterval && checkWindow && checkToken;
    }

    public long getWindowUpperLimit(long timestamp) {
        if (this.window != null) {
            return window.getEarliest(timestamp);
        }
        return timestamp;
    }

}
