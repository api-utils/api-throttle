package com.nobodyhub.transcendence.api.throttle.core.utils;

import org.junit.Test;

import static com.nobodyhub.transcendence.api.throttle.core.utils.StringUtils.isEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {
    @Test
    public void isEmptyTest() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertTrue(isEmpty(" "));
        assertTrue(isEmpty("null"));
        assertTrue(isEmpty(" null "));

        assertFalse(isEmpty("string"));
    }
}