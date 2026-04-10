package com.paxus.pay.poslinkui.demo.entry.navigation

import android.content.Intent
import android.os.Bundle
import com.paxus.pay.poslinkui.demo.entry.EntryActivity

/**
 * Single place to map incoming [Intent] to a [TransactionRoute] (FR-019). Extend when a screen moves to pure Compose.
 */
object EntryRouteResolver {

    /**
     * Chooses the Nav graph start destination for the current intent.
     */
    fun resolveStartRoute(intent: Intent): TransactionRoute =
        resolveStartRoute(intent.getBooleanExtra(EntryActivity.EXTRA_NAV_COMPOSE_DEMO, false))

    /**
     * JVM-testable overload (avoids Android [Intent] stubs in unit tests).
     */
    fun resolveStartRoute(composeDemoExtra: Boolean): TransactionRoute =
        if (composeDemoExtra) TransactionRoute.ComposeDemo else TransactionRoute.EntryMain
}

/**
 * Stable fingerprint for tests or effects keyed off Entry [android.content.Intent] content.
 *
 * @param revision Logical revision (e.g. [com.paxus.pay.poslinkui.demo.viewmodel.EntryUiState.revision]).
 */
fun Intent.entryContentFingerprint(revision: Int): String =
    entryContentFingerprint(action, revision, extras)

/**
 * JVM-testable fingerprint builder.
 */
fun entryContentFingerprint(action: String?, revision: Int, extras: Bundle?): String = buildString {
    append(action)
    append("|rev=").append(revision).append("|")
    extras?.keySet()?.sorted()?.forEach { key ->
        append(key).append("=").append(extras.get(key)).append(";")
    }
}
