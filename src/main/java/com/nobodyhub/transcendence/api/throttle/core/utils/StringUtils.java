package com.nobodyhub.transcendence.api.throttle.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utilities for handling String
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class StringUtils {
    /**
     * check whether the given string is empty(meaningless) or not
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null
                || "null".equalsIgnoreCase(str.trim())
                || "".equalsIgnoreCase(str.trim());
    }
}
