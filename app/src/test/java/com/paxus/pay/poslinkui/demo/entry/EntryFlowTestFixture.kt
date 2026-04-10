package com.paxus.pay.poslinkui.demo.entry

/**
 * Shared fixture for entry-flow focused unit tests.
 */
data class EntryFlowTestFixture(
    val action: String = "TEST_ACTION",
    val category: String = "TEST_CATEGORY",
    val optionsCount: Int = 0,
    val expectsConfirmedPayload: Boolean = false,
    val expectsIndexPayload: Boolean = false,
    val sessionId: String = "session-1",
    val fragmentName: String = "TestFragment"
) {
    fun asCoverageLabel(): String = "$category::$action"
}
