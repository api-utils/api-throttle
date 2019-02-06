package com.nobodyhub.transcendence.api.throttle.anno;

import java.lang.annotation.*;

/**
 * Annotation that restrict the speed to execute a method
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(SpeedLimits.class)
public @interface SpeedLimited {
    /**
     * The bucket name
     */
    String bucket();

    /**
     * Policy applied when the execution blocked
     */
    BlockPolicy whenBlocked() default BlockPolicy.WAIT;

    /**
     * Number of times to retrys
     */
    int retry() default 0;

    /**
     * delay between retries in millisecond
     */
    long retryDelay() default 200L;
}
