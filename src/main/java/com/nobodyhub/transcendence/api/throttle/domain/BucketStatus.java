package com.nobodyhub.transcendence.api.throttle.domain;

import lombok.Getter;

import static com.nobodyhub.transcendence.api.throttle.utils.NumberUtils.getNonNegative;
import static com.nobodyhub.transcendence.api.throttle.utils.NumberUtils.parseLong;

@Getter
public class BucketStatus {
    /**
     * number of tokens remaining for the bucket
     */
    private Long nToken;
    /**
     * millisecond of last request
     */
    private Long lastRequest;

    /**
     * number of token consumed in the windowed time
     */
    private Long nWindowed;

    public BucketStatus(String nToken, String lastRequest, Long nWindowed) {
        this.nToken = getNonNegative(parseLong(nToken));
        this.lastRequest = getNonNegative(parseLong(lastRequest));
        this.nWindowed = getNonNegative(nWindowed);
    }
}
