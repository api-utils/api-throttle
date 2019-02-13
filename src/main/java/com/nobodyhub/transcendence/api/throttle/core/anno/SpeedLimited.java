package com.nobodyhub.transcendence.api.throttle.core.anno;

import java.lang.annotation.*;

/**
 * Annotation that restrict the speed to execute a method
 * if multiple {@link SpeedLimited} applied on that same method, following limitation will be used:
 * <ul>
 * <li>the maximum {@link #retry()}</li>
 * <li>minimum {@link #retryDelay()}</li>
 * <li>longest {@link #waitTimeout()}</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SpeedLimited {
    /**
     * The bucket names
     */
    String[] buckets();

    /**
     * Policy applied when the execution blocked
     */
    BlockPolicy whenBlocked() default BlockPolicy.RETRY;

    /**
     * Number of times to retrys
     * checked only when {@link #whenBlocked()} ==  {@link BlockPolicy#RETRY}
     */
    int retry() default 3;

    /**
     * delay between retries in millisecond
     * checked only when {@link #whenBlocked()} ==  {@link BlockPolicy#RETRY}
     */
    long retryDelay() default 200L;

    /**
     * Time in millisecond to wait until get executed
     */
    long waitTimeout() default 10000L;
}
