package com.nobodyhub.transcendence.api.throttle.domain;

import lombok.Getter;

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
     * number of tocken consumed in the windowed time
     */
    private long nWindowed;

    public BucketStatus(Integer nToken, Long lastRequest, Long nWindowed) {
        if (nToken != null) {
            this.nToken = nToken;
        }

        if (lastRequest != null) {
            this.lastRequest = lastRequest;
        }

        if (nWindowed != null) {
            this.nWindowed = nWindowed;
        }
    }
}
