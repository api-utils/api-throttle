package com.nobodyhub.transcendence.api.throttle.bucket.domain;

import lombok.Getter;

import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.getNonNegative;
import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.parseLong;

@Getter
public class BucketStatus {
    /**
     * number of tokens remaining for the bucket
     */
    private long nToken;
    /**
     * millisecond of last request
     */
    private long lastRequest;
    /**
     * number of token consumed in the windowed time
     */
    private long nWindowed;

    public BucketStatus(String nToken, String lastRequest, Long nWindowed) {
        Long val = getNonNegative(parseLong(nToken));
        this.nToken = val == null ? 0 : val;

        val = getNonNegative(parseLong(lastRequest));
        this.lastRequest = val == null ? 0 : val;

        val = getNonNegative(nWindowed);
        this.nWindowed = val == null ? 0 : val;
    }
}
