package com.paxus.pay.poslinkui.demo.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ValuePatternUtilsTest {
    @Test
    fun getMaxLength_singleValue() {
        assertEquals(4, ValuePatternUtils.getMaxLength("4"))
    }

    @Test
    fun getMinLength_singleValue() {
        assertEquals(4, ValuePatternUtils.getMinLength("4"))
    }

    /** Failure path: invalid / empty pattern must not silently return 0 with unsafe defaults. */
    @Test
    fun getMaxLength_empty_throws() {
        assertThrows(IllegalArgumentException::class.java) {
            ValuePatternUtils.getMaxLength("")
        }
    }
}
