package com.paxus.pay.poslinkui.demo.entry.text

/**
 * Classifies a [com.pax.us.pay.ui.constant.entry.TextEntry] action for Compose routing (US1 / T007–T009).
 */
sealed class TextEntryKind {
    /**
     * Currency-style amount in minor units (cents); response key is an [com.pax.us.pay.ui.constant.entry.EntryRequest] long param.
     */
    data class AmountMinor(
        val responseKey: String
    ) : TextEntryKind()

    /** Single string line; response key is an [com.pax.us.pay.ui.constant.entry.EntryRequest] string param. */
    data class SingleString(
        val responseKey: String
    ) : TextEntryKind()

    /** Address + zip per [com.pax.us.pay.ui.constant.entry.TextEntry.ACTION_ENTER_AVS_DATA]. */
    object Avs : TextEntryKind()

    /** Dynamic FSA amount lines per [com.pax.us.pay.ui.constant.entry.EntryExtraData.PARAM_FSA_AMOUNT_OPTIONS]. */
    object Fsa : TextEntryKind()

    /** Action is not mapped; UI should show the unmapped placeholder. */
    object Unknown : TextEntryKind()
}
