package com.paxus.pay.poslinkui.demo.entry.text

import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.TextEntry
import com.paxus.pay.poslinkui.demo.entry.EntryActionRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

/**
 * [TextEntryResponseParams] maps each Text Entry action to the [EntryRequest] key used in
 * [com.paxus.pay.poslinkui.demo.viewmodel.EntryViewModel.sendNext] (amount = long, line entry = string).
 * [TextEntry.ACTION_ENTER_AMOUNT] is handled only in [com.paxus.pay.poslinkui.demo.entry.compose.EntryScreenRouter]
 * with [EntryRequest.PARAM_AMOUNT], so [TextEntryResponseParams.resolveKind] correctly returns [TextEntryKind.Unknown].
 */
class TextEntryResponseParamsTest {

    @Test
    fun resolveKind_mapsAmountActions() {
        assertEquals(
            EntryRequest.PARAM_TIP,
            (TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_TIP) as TextEntryKind.AmountMinor).responseKey
        )
        assertEquals(
            EntryRequest.PARAM_FUEL_AMOUNT,
            (TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_FUEL_AMOUNT) as TextEntryKind.AmountMinor).responseKey
        )
    }

    @Test
    @Suppress("DEPRECATION")
    fun resolveKind_mapsStringActions() {
        assertEquals(
            EntryRequest.PARAM_ADDRESS,
            (TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_ADDRESS) as TextEntryKind.SingleString).responseKey
        )
        assertEquals(
            EntryRequest.PARAM_MERCHANT_TAX_ID,
            (TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_MERCHANT_TAX_ID) as TextEntryKind.SingleString).responseKey
        )
        assertEquals(
            EntryRequest.PARAM_FLEET_CUSTOMER_DATA,
            (TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_FLEET_DATA) as TextEntryKind.SingleString).responseKey
        )
        assertEquals(
            EntryRequest.PARAM_VISA_TRANSID,
            (TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID) as TextEntryKind.SingleString).responseKey
        )
        assertEquals(
            EntryRequest.PARAM_VISA_PLAN_ACCEPTANCE_ID,
            (TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID) as TextEntryKind.SingleString).responseKey
        )
    }

    @Test
    fun resolveKind_specialScreens() {
        assertTrue(TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_AVS_DATA) is TextEntryKind.Avs)
        assertTrue(TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_FSA_DATA) is TextEntryKind.Fsa)
    }

    @Test
    fun resolveKind_unknownForNullAndGarbage() {
        assertTrue(TextEntryResponseParams.resolveKind(null) is TextEntryKind.Unknown)
        assertTrue(TextEntryResponseParams.resolveKind("not.a.real.action") is TextEntryKind.Unknown)
    }

    @Test
    fun resolveKind_enterAmount_unknownBecauseRouterUsesParamAmount() {
        assertTrue(
            TextEntryResponseParams.resolveKind(TextEntry.ACTION_ENTER_AMOUNT) is TextEntryKind.Unknown
        )
        assertTrue(
            "Amount Entry submits minor units under ${EntryRequest.PARAM_AMOUNT}",
            EntryRequest.PARAM_AMOUNT.isNotBlank()
        )
    }

    @Test
    fun everyRegisteredTextEntryAction_hasMappedResponseKindExceptEnterAmount() {
        for (action in EntryActionRegistry.textEntryActions) {
            val kind = TextEntryResponseParams.resolveKind(action)
            when (action) {
                TextEntry.ACTION_ENTER_AMOUNT ->
                    assertTrue(
                        "$action must stay Unknown; amount route uses ${EntryRequest.PARAM_AMOUNT}",
                        kind is TextEntryKind.Unknown
                    )
                else ->
                    when (kind) {
                        is TextEntryKind.AmountMinor,
                        is TextEntryKind.SingleString,
                        TextEntryKind.Avs,
                        TextEntryKind.Fsa -> Unit
                        is TextEntryKind.Unknown ->
                            fail("Registered text action $action must map to a response kind (registry vs TextEntryResponseParams drift)")
                    }
            }
        }
    }

    @Test
    fun registeredTextEntry_stringAndAmountKinds_useNonBlankEntryRequestKeys() {
        for (action in EntryActionRegistry.textEntryActions) {
            if (action == TextEntry.ACTION_ENTER_AMOUNT) continue
            when (val kind = TextEntryResponseParams.resolveKind(action)) {
                is TextEntryKind.AmountMinor ->
                    assertTrue(
                        "Action $action response key must match host contract",
                        kind.responseKey.isNotBlank()
                    )
                is TextEntryKind.SingleString ->
                    assertTrue(
                        "Action $action response key must match host contract",
                        kind.responseKey.isNotBlank()
                    )
                else -> Unit
            }
        }
    }
}
