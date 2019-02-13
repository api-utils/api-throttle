package com.nobodyhub.transcendence.api.throttle.core.anno;

import com.nobodyhub.transcendence.api.throttle.bucket.service.ThrottleBucketService;

import java.util.concurrent.TimeUnit;

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
    /**
     * Retry several times before give up
     */
    RETRY,
    ;

    /**
     * Apply the BlockPolicy to bucket status updates
     *
     * @param bucketService
     * @param speedLimited
     * @return true if OK to proceed status update
     * @throws InterruptedException
     */
    public boolean apply(ThrottleBucketService bucketService, SpeedLimited speedLimited) throws InterruptedException {
        switch (this) {
            case WAIT: {
                return applyWait(bucketService, speedLimited);
            }
            case SKIP: {
                return applySkip(bucketService, speedLimited);
            }
            case RETRY: {
                return applyRetry(bucketService, speedLimited);
            }
            default: {
                throw new UnhandledBlockPolicyException(this);
            }
        }
    }

    /**
     * @see BlockPolicy#RETRY
     */
    private boolean applyRetry(ThrottleBucketService bucketService, SpeedLimited speedLimited) throws InterruptedException {
        String[] buckets = speedLimited.buckets();
        int retry = speedLimited.retry();
        long delay = speedLimited.retryDelay();
        boolean approved = false;
        while (!approved && --retry >= 0) {
            approved = bucketService.checkBucket(buckets);
            if (!approved && delay > 0) {
                Thread.sleep(delay);
            }
        }
        return approved;
    }

    /**
     * @see BlockPolicy#SKIP
     */
    private boolean applySkip(ThrottleBucketService bucketService, SpeedLimited speedLimited) {
        String[] buckets = speedLimited.buckets();
        return bucketService.checkBucket(buckets);
    }

    /**
     * @see BlockPolicy#WAIT
     */
    private boolean applyWait(ThrottleBucketService bucketService, SpeedLimited speedLimited) {
        long nano = TimeUnit.NANOSECONDS.convert(speedLimited.waitTimeout(), TimeUnit.MILLISECONDS);
        String[] buckets = speedLimited.buckets();
        long start = System.nanoTime();
        boolean approved = false;
        while (!approved && (System.nanoTime() - start <= nano)) {
            approved = bucketService.checkBucket(buckets);
        }
        return approved;
    }
}

/**
 * Exception when no handler found for the block policy
 */
class UnhandledBlockPolicyException extends RuntimeException {
    UnhandledBlockPolicyException(BlockPolicy blockPolicy) {
        super(String.format("No rule found to handle BlockPolicy[%s]!", blockPolicy));
    }
}
