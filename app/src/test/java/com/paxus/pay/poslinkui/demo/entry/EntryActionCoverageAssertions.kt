package com.paxus.pay.poslinkui.demo.entry

import org.junit.Assert.assertTrue

/**
 * Assertions for action-level route/submit/writeback checks.
 */
object EntryActionCoverageAssertions {

    fun assertRoutable(fixture: EntryFlowTestFixture, actual: Boolean) {
        assertTrue("${fixture.asCoverageLabel()} should be routable", actual)
    }

    fun assertSubmittable(fixture: EntryFlowTestFixture, actual: Boolean) {
        assertTrue("${fixture.asCoverageLabel()} should be submittable", actual)
    }

    fun assertWritable(fixture: EntryFlowTestFixture, actual: Boolean) {
        assertTrue("${fixture.asCoverageLabel()} should be writable", actual)
    }
}
