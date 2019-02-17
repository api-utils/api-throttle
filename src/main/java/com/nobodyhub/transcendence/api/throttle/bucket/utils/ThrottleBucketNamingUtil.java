package com.nobodyhub.transcendence.api.throttle.bucket.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;


/**
 * Naming utils for bucket related keys in redis
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThrottleBucketNamingUtil {
    /**
     * name for the bucket status
     */
    public static String status(@NonNull String bucket) {
        return bucket + ":status";
    }

    /**
     * name for the bucket request window
     */
    public static String window(@NonNull String bucket) {
        return bucket + ":window";
    }
}
