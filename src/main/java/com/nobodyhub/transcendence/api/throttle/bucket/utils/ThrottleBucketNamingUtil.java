package com.nobodyhub.transcendence.api.throttle.bucket.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThrottleBucketNamingUtil {
    public static String status(@NonNull String bucket) {
        return bucket + ":status";
    }

    public static String window(@NonNull String bucket) {
        return bucket + ":window";
    }

    public static String execution(@NonNull String bucket, @NonNull String execToken) {
        return bucket + ":exec:" + execToken;
    }
}
