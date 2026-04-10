package com.paxus.pay.poslinkui.demo.ui.device

/**
 * Logical device profile keys aligned with [specs/002-compose-migration/device-profiles.md] P0 / full table.
 * Resolved in one place — do not branch on [android.os.Build.MODEL] inside screens.
 */
enum class DeviceProfileId {
    /** Default / unknown — conservative phone-like spacing */
    STANDARD_PHONE,

    /** P0: A3700 — large landscape-class (sw ~533) */
    A3700,

    /** P0: A920MAX — tall narrow portrait */
    A920MAX,

    /** P0: A920Pro (PCI7) + other 720×1440-class terminals */
    A920_CLASS,

    /** P0: A35 — small mdpi */
    A35,

    /** P0: A80 / A80S — small portrait */
    A80_CLASS
}
