package com.paxus.pay.poslinkui.demo.entry.navigation

/**
 * Type-safe Nav destinations for Entry (FR-019). String routes match Nav `composable` registrations.
 */
sealed class TransactionRoute(val route: String) {
    /** Primary Entry graph: screen chosen from [com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel.uiState]. */
    object EntryMain : TransactionRoute("entry_main")

    /** Verification destination (see [com.paxus.pay.poslinkui.demo.entry.EntryActivity.EXTRA_NAV_COMPOSE_DEMO]). */
    object ComposeDemo : TransactionRoute("compose_demo")
}
