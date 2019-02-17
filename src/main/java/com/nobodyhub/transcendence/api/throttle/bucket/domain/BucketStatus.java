package com.nobodyhub.transcendence.api.throttle.bucket.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode
@RequiredArgsConstructor
public class BucketStatus {
    /**
     * name of the bucket
     */
    private String bucket;
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
}
