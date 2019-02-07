package com.nobodyhub.transcendence.api.throttle.core.anno;

import java.lang.annotation.*;

/**
 * Container annotation that aggregates several {@link SpeedLimited} annotations.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SpeedLimits {
    SpeedLimited[] value();
}
