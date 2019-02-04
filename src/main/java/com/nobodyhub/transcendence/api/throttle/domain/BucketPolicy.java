package com.nobodyhub.transcendence.api.throttle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BucketPolicy {
    /**
     * The window size in second to be measured.
     * -1 means infinite window size will be applied
     */
    private int window;
    /**
     * Number of tokens that are available for {@link #window}.
     * -1 means no limitation on the number of the tokens
     */
    private int nToken;
    /**
     * Minimum interval between requests
     * 0 means no interval required between requests
     */
    private int interval;
}
