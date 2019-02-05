package com.nobodyhub.transcendence.api.throttle.domain;

import lombok.Getter;

@Getter
public class BucketPolicy {
    /**
     * The window size in second to be measured. non-zero, positive
     * Long.MAX_VALUE means infinite window size will be applied
     */
    private long window = Long.MAX_VALUE;
    /**
     * Number of tokens that are available for {@link #window}. non-zero, positive
     * Long.MAX_VALUE means no limitation on the number of the tokens
     */
    private long nToken = Long.MAX_VALUE;
    /**
     * Minimum interval between requests, non-negative
     * 0 means no interval required between requests
     */
    private long interval = 0;

    public BucketPolicy(Long window, Long nToken, Long interval) {
        if (window != null) {
            this.window = window < 0 ? 0 : window;
        }
        if (nToken != null) {
            this.nToken = nToken < 0 ? 0 : nToken;

        }
        if (interval != null) {
            this.interval = interval < 0 ? 0 : interval;
        }
    }

    public boolean check(Long timestamp, BucketStatus status) {
        boolean checkInterval = interval == 0 || timestamp - status.getLastRequest() > interval;
        boolean checkWindow = window == -1 || nToken != -1 || status.getNWindowed() < nToken;
        boolean checkToken = status.getNToken() > 0;
        return checkInterval && checkWindow && checkToken;
    }

    public long assignToken(long timeCollapse, BucketStatus status) {
        return timeCollapse * nToken / window;
    }
}
