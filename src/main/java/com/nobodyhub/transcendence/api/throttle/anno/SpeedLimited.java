package com.nobodyhub.transcendence.api.throttle.anno;

import java.lang.annotation.*;

/**
 * Annotation that restrict the speed to execute a method
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
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
     * -1 means no limit on retry times
     */
    int retry() default -1;
}
