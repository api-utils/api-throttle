package com.nobodyhub.transcendence.api.throttle.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;


/**
 * Utilities for Number(Integer, Long, and etc.) objects
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class NumberUtils {
    /**
     * Parse String to Long
     *
     * @param str
     * @return null if parse fail
     */
    @Nullable
    public static Long parseLong(String str) {
        try {
            if (!StringUtils.isEmpty(str)) {
                return Long.valueOf(str);
            }
        } catch (NumberFormatException e) {
            log.error("Error happens when parsing {} into Long.", str);
        }
        return null;
    }

    /**
     * convert negative values to zero.
     *
     * @param value
     * @return
     */
    @Nullable
    public static Long getNonNegative(Long value) {
        if (value == null) {
            return null;
        }
        return value < 0 ? 0 : value;
    }
}
