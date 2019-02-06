package com.nobodyhub.transcendence.api.throttle.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.nobodyhub.transcendence.api.throttle.utils.NumberUtils.getNonNegative;
import static com.nobodyhub.transcendence.api.throttle.utils.NumberUtils.parseLong;

@Getter
@Slf4j
public class BucketPolicy {
    /**
     * The window size in second to be measured. non-zero, positive
     * null value means infinite window size will be applied
     */
    private Long window;
    /**
     * Number of tokens that are available for {@link #window}. non-zero, positive
     * null value means no limitation on the number of the tokens
     */
    private Long nToken;
    /**
     * Minimum interval between requests, non-negative
     * null value means no interval required between requests
     */
    private Long interval;

    public BucketPolicy(String windowStr, String nTokenStr, String intervalStr) {
        this.window = getNonNegative(parseLong(windowStr));
        this.nToken = getNonNegative(parseLong(nTokenStr));
        this.interval = getNonNegative(parseLong(intervalStr));
    }

    public BucketPolicy(Long nToken) {
        this.nToken = getNonNegative(nToken);
    }

    public boolean check(Long timestamp, BucketStatus status) {
        boolean checkInterval = interval == null || timestamp - status.getLastRequest() > interval;
        boolean checkWindow = window == null || nToken == null || status.getNWindowed() < nToken;
        boolean checkToken = status.getNToken() > 0;
        return checkInterval && checkWindow && checkToken;
    }

    public long assignToken(long timeCollapse, BucketStatus status) {
        if (nToken != null && window != null) {
            return status.getNToken() - 1 + timeCollapse * nToken / window;
        }
        return status.getNToken() - 1;
    }

    public long getWindowUpperLimit(long timestamp) {
        if (this.window != null) {
            return timestamp - this.window;
        }
        return timestamp;
    }

}
