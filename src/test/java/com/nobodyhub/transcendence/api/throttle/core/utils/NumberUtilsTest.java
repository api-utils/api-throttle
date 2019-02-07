package com.nobodyhub.transcendence.api.throttle.core.utils;

import org.junit.Test;

import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.getNonNegative;
import static com.nobodyhub.transcendence.api.throttle.core.utils.NumberUtils.parseLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NumberUtilsTest {
    @Test
    public void parseLongTest() {
        assertEquals((Long) 1L, parseLong("1"));
        assertEquals((Long) 12123123123123131L, parseLong("12123123123123131"));
        assertNull(parseLong("ABCD"));
        assertNull(parseLong(null));
        assertNull(parseLong("null"));
        assertNull(parseLong(""));
    }

    @Test
    public void getNonNegativeTest() {
        assertEquals((Long) 1L, getNonNegative(1L));
        assertEquals((Long) 0L, getNonNegative(-1L));
        assertNull(getNonNegative(null));
    }
}