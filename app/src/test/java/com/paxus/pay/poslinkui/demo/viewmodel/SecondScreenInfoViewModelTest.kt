package com.paxus.pay.poslinkui.demo.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

class SecondScreenInfoViewModelTest {
    @Test
    fun buildScreenInfo_preservesInputValues() {
        val vm = SecondScreenInfoViewModel()
        val info = vm.buildScreenInfo("100.00", "msg", "APPROVED", 1, "title", "ok")
        assertEquals("100.00", info.amount)
        assertEquals("msg", info.msg)
        assertEquals("APPROVED", info.status)
        assertEquals(1, info.imageResourceId)
        assertEquals("title", info.title)
        assertEquals("ok", info.statusTitle)
    }

    /** Failure / edge path: nullable fields stay null (no forced defaults). */
    @Test
    fun buildScreenInfo_allNulls_preserved() {
        val vm = SecondScreenInfoViewModel()
        val info = vm.buildScreenInfo(null, null, null, null, null, null)
        assertEquals(null, info.amount)
        assertEquals(null, info.msg)
        assertEquals(null, info.status)
        assertEquals(null, info.imageResourceId)
        assertEquals(null, info.title)
        assertEquals(null, info.statusTitle)
    }
}
