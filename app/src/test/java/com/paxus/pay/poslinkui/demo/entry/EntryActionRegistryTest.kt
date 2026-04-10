package com.paxus.pay.poslinkui.demo.entry

import com.pax.us.pay.ui.constant.entry.TextEntry
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.InformationEntry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EntryActionRegistryTest {

    @Test
    fun isKnownEntryAction_unknownCategory_returnsFalse() {
        assertFalse(EntryActionRegistry.isKnownEntryAction("UNKNOWN_ACTION", setOf("UNKNOWN_CATEGORY")))
    }

    @Test
    fun isKnownEntryAction_knownTextAction_returnsTrue() {
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                TextEntry.ACTION_ENTER_AMOUNT,
                setOf(TextEntry.CATEGORY)
            )
        )
    }

    @Test
    fun isKnownEntryAction_knownVisaInstallmentTextActions_returnTrue() {
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                TextEntry.ACTION_ENTER_VISA_INSTALLMENT_TRANSACTIONID,
                setOf(TextEntry.CATEGORY)
            )
        )
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                TextEntry.ACTION_ENTER_VISA_INSTALLMENT_PLAN_ACCEPTANCE_ID,
                setOf(TextEntry.CATEGORY)
            )
        )
    }

    @Test
    fun isKnownEntryAction_knownOptionAction_returnsTrue() {
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                OptionEntry.ACTION_SELECT_CURRENCY,
                setOf(OptionEntry.CATEGORY)
            )
        )
    }

    @Test
    @Suppress("DEPRECATION")
    fun isKnownEntryAction_knownInstallmentOptionActions_returnTrue() {
        val optionCategory = setOf(OptionEntry.CATEGORY)
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                OptionEntry.ACTION_SELECT_TIP_AMOUNT,
                optionCategory
            )
        )
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                OptionEntry.ACTION_SELECT_CASHBACK_AMOUNT,
                optionCategory
            )
        )
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                OptionEntry.ACTION_SELECT_INSTALLMENT_PLAN,
                optionCategory
            )
        )
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                OptionEntry.ACTION_SELECT_TRANS_FOR_ADJUST,
                optionCategory
            )
        )
    }

    @Test
    fun isKnownEntryAction_knownSecurityAction_returnsTrue() {
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                SecurityEntry.ACTION_ENTER_PIN,
                setOf(SecurityEntry.CATEGORY)
            )
        )
    }

    @Test
    fun isKnownEntryAction_knownConfirmationAction_returnsTrue() {
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                ConfirmationEntry.ACTION_CONFIRM_TOTAL_AMOUNT,
                setOf(ConfirmationEntry.CATEGORY)
            )
        )
    }

    @Test
    fun isKnownEntryAction_us2GapConfirmationActions_returnTrue() {
        val category = setOf(ConfirmationEntry.CATEGORY)
        assertTrue(EntryActionRegistry.isKnownEntryAction(ConfirmationEntry.ACTION_CONFIRM_BATCH_CLOSE_WITH_INCOMPLETE_TRANSACTION, category))
        assertTrue(EntryActionRegistry.isKnownEntryAction(ConfirmationEntry.ACTION_CONFIRM_BATCH_FOR_APPLICATION_UPDATE, category))
        assertTrue(EntryActionRegistry.isKnownEntryAction(EntryGapActions.ACTION_CONFIRM_DEBIT_TRANS, category))
        assertTrue(EntryActionRegistry.isKnownEntryAction(ConfirmationEntry.ACTION_CONFIRM_ONLINE_RETRY_OFFLINE, category))
        assertTrue(EntryActionRegistry.isKnownEntryAction(ConfirmationEntry.ACTION_CONFIRM_TAX_AMOUNT, category))
    }

    @Test
    fun isKnownEntryAction_us3GapInformationAction_returnTrue() {
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                EntryGapActions.ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END,
                setOf(InformationEntry.CATEGORY)
            )
        )
    }
}
