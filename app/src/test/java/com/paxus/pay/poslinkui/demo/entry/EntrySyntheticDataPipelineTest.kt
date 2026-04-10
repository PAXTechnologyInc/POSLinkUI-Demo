package com.paxus.pay.poslinkui.demo.entry

import android.content.res.Resources
import android.os.Bundle
import com.pax.us.pay.ui.constant.entry.ConfirmationEntry
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.pax.us.pay.ui.constant.entry.EntryRequest
import com.pax.us.pay.ui.constant.entry.InformationEntry
import com.pax.us.pay.ui.constant.entry.OptionEntry
import com.pax.us.pay.ui.constant.entry.PoslinkEntry
import com.pax.us.pay.ui.constant.entry.SecurityEntry
import com.pax.us.pay.ui.constant.entry.SignatureEntry
import com.pax.us.pay.ui.constant.entry.TextEntry
import com.pax.us.pay.ui.constant.entry.enumeration.AdminPasswordType
import com.pax.us.pay.ui.constant.entry.enumeration.CurrencyType
import com.pax.us.pay.ui.constant.entry.enumeration.FSAAmountType
import com.pax.us.pay.ui.constant.entry.enumeration.PanStyles
import com.pax.us.pay.ui.constant.entry.enumeration.PinStyles
import com.pax.us.pay.ui.constant.status.StatusData
import com.paxus.pay.poslinkui.demo.entry.compose.parsePoslinkMessageList
import com.paxus.pay.poslinkui.demo.entry.confirmation.ConfirmationMessageFormatter
import com.paxus.pay.poslinkui.demo.entry.option.OptionEntryMessageFormatter
import com.paxus.pay.poslinkui.demo.entry.security.SecurityMessageFormatter
import com.paxus.pay.poslinkui.demo.entry.text.TextEntryKind
import com.paxus.pay.poslinkui.demo.entry.text.TextEntryMessageFormatter
import com.paxus.pay.poslinkui.demo.entry.text.TextEntryResponseParams
import com.paxus.pay.poslinkui.demo.utils.ValuePatternUtils
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Feeds synthetic host-style extras through the same non-Compose parsing/formatting stack used before
 * drawing Entry screens — **without** starting [EntryActivity]. [stubResources] avoids real string
 * table lookups (stubs only); the goal is to catch throws in branching / parsing, not literal copy.
 *
 * **Note:** With `android { testOptions { unitTests.isReturnDefaultValues = true } }`, [Bundle.getString]
 * for missing keys
 * can yield a **non-empty placeholder** and two-argument `getString(key, default)` may not return
 * `default`. Baseline [putString]/[putBoolean]/[putLong] values below mirror “host cleared” extras so
 * enum/style branches match production.
 */
class EntrySyntheticDataPipelineTest {

    private val res: Resources = StubTestResources()

    @Test
    fun textCategory_syntheticExtras_pipelineDoesNotThrow() {
        EntryActionRegistry.textEntryActions.forEach { action ->
            val extras = Bundle().apply { fillTextCategoryExtras(action) }
            assertTrue(
                entrySmokeLabel("text", action),
                EntryActionRegistry.isKnownEntryAction(action, setOf(TextEntry.CATEGORY))
            )
            runCatching { exerciseTextPipeline(action, extras) }
                .onFailure { t ->
                    throw AssertionError("${entrySmokeLabel("text", action)}: ${t.message}", t)
                }
        }
    }

    @Test
    fun securityCategory_syntheticExtras_promptDoesNotThrow() {
        EntryActionRegistry.securityEntryActions.forEach { action ->
            val extras = Bundle().apply { fillSecurityCategoryBaseline() }
            assertTrue(
                entrySmokeLabel("security", action),
                EntryActionRegistry.isKnownEntryAction(action, setOf(SecurityEntry.CATEGORY))
            )
            runCatching { SecurityMessageFormatter.prompt(action, extras, res) }
                .onFailure { t ->
                    throw AssertionError("${entrySmokeLabel("security", action)}: ${t.message}", t)
                }
        }
    }

    @Test
    fun optionCategory_syntheticExtras_titleDoesNotThrow() {
        EntryActionRegistry.optionEntryActions.forEach { action ->
            val extras = Bundle().apply {
                fillOptionCategoryBaseline()
                putStringArray(EntryExtraData.PARAM_OPTIONS, arrayOf("Option A", "Option B"))
                if (action == OptionEntry.ACTION_SELECT_CURRENCY) {
                    putString(EntryExtraData.PARAM_EXCHANGE_RATE, "Rate: 1.0")
                }
            }
            assertTrue(
                entrySmokeLabel("option", action),
                EntryActionRegistry.isKnownEntryAction(action, setOf(OptionEntry.CATEGORY))
            )
            runCatching { OptionEntryMessageFormatter.title(action, extras, res) }
                .onFailure { t ->
                    throw AssertionError("${entrySmokeLabel("option", action)}: ${t.message}", t)
                }
        }
    }

    @Test
    fun confirmationCategory_syntheticExtras_presentationDoesNotThrow() {
        EntryActionRegistry.confirmationEntryActions.forEach { action ->
            val extras = confirmationExtras(action)
            assertTrue(
                entrySmokeLabel("confirmation", action),
                EntryActionRegistry.isKnownEntryAction(action, setOf(ConfirmationEntry.CATEGORY))
            )
            runCatching { ConfirmationMessageFormatter.build(action, extras, res) }
                .onFailure { t ->
                    throw AssertionError("${entrySmokeLabel("confirmation", action)}: ${t.message}", t)
                }
        }
    }

    @Test
    fun informationCategory_syntheticExtras_readsDoNotThrow() {
        EntryActionRegistry.informationEntryActions.forEach { action ->
            val extras = informationExtras(action)
            assertTrue(
                entrySmokeLabel("information", action),
                EntryActionRegistry.isKnownEntryAction(action, setOf(InformationEntry.CATEGORY))
            )
            runCatching { exerciseInformationExtras(action, extras) }
                .onFailure { t ->
                    throw AssertionError("${entrySmokeLabel("information", action)}: ${t.message}", t)
                }
        }
    }

    @Test
    fun signatureCategory_registryAcceptsIntent() {
        assertTrue(
            EntryActionRegistry.isKnownEntryAction(
                SignatureEntry.ACTION_SIGNATURE,
                setOf(SignatureEntry.CATEGORY)
            )
        )
    }

    @Test
    fun poslinkCategory_syntheticExtras_parseDoesNotThrow() {
        EntryActionRegistry.poslinkEntryActions.forEach { action ->
            val extras = poslinkExtras(action)
            assertTrue(
                entrySmokeLabel("poslink", action),
                EntryActionRegistry.isKnownEntryAction(action, setOf(PoslinkEntry.CATEGORY))
            )
            runCatching { exercisePoslinkExtras(action, extras) }
                .onFailure { t ->
                    throw AssertionError("${entrySmokeLabel("poslink", action)}: ${t.message}", t)
                }
        }
    }

    private fun exerciseTextPipeline(action: String, extras: Bundle) {
        if (action == TextEntry.ACTION_ENTER_AMOUNT) {
            val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
            TextEntryMessageFormatter.amountKindPrompt(action, extras, currency, res)
            runCatching {
                ValuePatternUtils.getMaxLength(extras.getString(EntryExtraData.PARAM_VALUE_PATTERN, "1-12"))
            }
            return
        }
        when (TextEntryResponseParams.resolveKind(action)) {
            is TextEntryKind.AmountMinor -> {
                val currency = extras.getString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
                TextEntryMessageFormatter.amountKindPrompt(action, extras, currency, res)
                extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)?.let { pattern ->
                    runCatching { ValuePatternUtils.getMaxLength(pattern) }
                }
            }
            is TextEntryKind.SingleString -> {
                TextEntryMessageFormatter.singleLinePrompt(action, extras, res)
                extras.getString(EntryExtraData.PARAM_VALUE_PATTERN)?.let { pattern ->
                    runCatching { ValuePatternUtils.getMaxLength(pattern) }
                }
            }
            TextEntryKind.Avs -> {
                runCatching {
                    ValuePatternUtils.getMaxLength(
                        extras.getString(EntryExtraData.PARAM_ADDRESS_PATTERN, "1-30")
                    )
                    ValuePatternUtils.getMaxLength(
                        extras.getString(EntryExtraData.PARAM_ZIP_CODE_PATTERN, "1-10")
                    )
                }
            }
            TextEntryKind.Fsa -> {
                extras.getStringArray(EntryExtraData.PARAM_FSA_AMOUNT_OPTIONS)?.forEach { opt ->
                    TextEntryMessageFormatter.fsaAmountLinePrompt(opt, res)
                }
            }
            TextEntryKind.Unknown ->
                throw AssertionError("Unexpected Unknown kind for registered text action=$action")
        }
    }

    private fun exerciseInformationExtras(action: String, extras: Bundle) {
        when (action) {
            InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE -> {
                extras.getString(EntryExtraData.PARAM_CARD_TYPE)
                extras.getString(StatusData.PARAM_MSG_PRIMARY)
            }
            EntryGapActions.ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END -> {
                extras.getString(EntryExtraData.PARAM_TITLE)
                extras.getString(EntryExtraData.PARAM_MESSAGE)
            }
            else -> {
                val keys = extras.getStringArray(EntryExtraData.PARAM_INFORMATION_KEY)
                val values = extras.getStringArray(EntryExtraData.PARAM_INFORMATION_VALUE)
                if (keys != null && values != null) {
                    for (i in keys.indices) {
                        if (i < values.size) {
                            keys[i]
                            values[i]
                        }
                    }
                }
            }
        }
    }

    private fun exercisePoslinkExtras(action: String, extras: Bundle) {
        when (action) {
            PoslinkEntry.ACTION_SHOW_MESSAGE -> {
                val raw = extras.getString(EntryExtraData.PARAM_MESSAGE_LIST).orEmpty()
                parsePoslinkMessageList(raw)
            }
            else -> Unit
        }
    }

    private fun Bundle.fillTextCategoryExtras(action: String) {
        when (action) {
            TextEntry.ACTION_ENTER_TIP -> {
                putStringArray(EntryExtraData.PARAM_TIP_OPTIONS, arrayOf("15%", "18%", "20%"))
                putLong(EntryRequest.PARAM_AMOUNT, 2_500L)
            }
            TextEntry.ACTION_ENTER_CASH_BACK -> {
                putStringArray(EntryExtraData.PARAM_CASHBACK_OPTIONS, arrayOf("$1.00", "$2.00"))
            }
            TextEntry.ACTION_ENTER_FSA_DATA -> {
                putStringArray(
                    EntryExtraData.PARAM_FSA_AMOUNT_OPTIONS,
                    arrayOf(FSAAmountType.HEALTH_CARE_AMOUNT)
                )
                putString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
            }
            TextEntry.ACTION_ENTER_AVS_DATA -> {
                putString(EntryExtraData.PARAM_ADDRESS_PATTERN, "1-30")
                putString(EntryExtraData.PARAM_ZIP_CODE_PATTERN, "1-10")
            }
            else -> {
                putString(EntryExtraData.PARAM_VALUE_PATTERN, "1-12")
                putString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
                putString(EntryExtraData.PARAM_TRANS_TYPE, "SMOKE TEST")
            }
        }
    }

    private fun confirmationExtras(action: String): Bundle = Bundle().apply {
        fillConfirmationCategoryBaseline()
        when (action) {
            ConfirmationEntry.ACTION_CONFIRM_SERVICE_FEE -> {
                putLong(EntryExtraData.PARAM_TOTAL_AMOUNT, 5000L)
                putLong(EntryExtraData.PARAM_SERVICE_FEE, 150L)
                putString(EntryExtraData.PARAM_SERVICE_FEE_NAME, "Fee")
                putString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
            }
            ConfirmationEntry.ACTION_CONFIRM_SURCHARGE_FEE -> {
                putLong(EntryExtraData.PARAM_SURCHARGE_FEE, 99L)
                putString(EntryExtraData.PARAM_SURCHARGE_FEE_NAME, "Surcharge")
                putString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
            }
            ConfirmationEntry.ACTION_REVERSE_PARTIAL_APPROVAL,
            ConfirmationEntry.ACTION_SUPPLEMENT_PARTIAL_APPROVAL -> {
                putString(StatusData.PARAM_MSG_PRIMARY, "Partial")
            }
            ConfirmationEntry.ACTION_DISPLAY_QR_CODE_RECEIPT -> {
                putString(EntryExtraData.PARAM_QR_CODE_CONTENT, "smoke-qr")
            }
            else -> Unit
        }
    }

    private fun informationExtras(action: String): Bundle = Bundle().apply {
        when (action) {
            InformationEntry.ACTION_DISPLAY_APPROVE_MESSAGE -> {
                putString(EntryExtraData.PARAM_CARD_TYPE, "VISA")
                putString(StatusData.PARAM_MSG_PRIMARY, "Approved")
            }
            EntryGapActions.ACTION_DISPLAY_VISA_INSTALLMENT_TRANSACTION_END -> {
                putString(EntryExtraData.PARAM_TITLE, "End")
                putString(EntryExtraData.PARAM_MESSAGE, "Done")
            }
            else -> {
                putStringArray(EntryExtraData.PARAM_INFORMATION_KEY, arrayOf("Label"))
                putStringArray(EntryExtraData.PARAM_INFORMATION_VALUE, arrayOf("Value"))
            }
        }
    }

    private fun poslinkExtras(action: String): Bundle = Bundle().apply {
        when (action) {
            PoslinkEntry.ACTION_INPUT_TEXT -> {
                putString(EntryExtraData.PARAM_TITLE, "Smoke input")
                putString(EntryExtraData.PARAM_MIN_LENGTH, "1")
                putString(EntryExtraData.PARAM_MAX_LENGTH, "12")
                putString(EntryExtraData.PARAM_INPUT_TYPE, "0")
            }
            PoslinkEntry.ACTION_SHOW_MESSAGE -> {
                putString(EntryExtraData.PARAM_TITLE, "Smoke message")
                putString(EntryExtraData.PARAM_MESSAGE_LIST, "Line one\nLine two")
            }
            PoslinkEntry.ACTION_SHOW_ITEM -> {
                putString(EntryExtraData.PARAM_TITLE, "Items")
                putString(EntryExtraData.PARAM_MESSAGE_LIST, "Item A\nItem B")
            }
            PoslinkEntry.ACTION_SHOW_THANK_YOU -> {
                putString(EntryExtraData.PARAM_TITLE, "Thanks")
                putString(EntryExtraData.PARAM_MESSAGE_1, "M1")
            }
            PoslinkEntry.ACTION_SHOW_DIALOG -> {
                putString(EntryExtraData.PARAM_TITLE, "Pick")
                putStringArray(EntryExtraData.PARAM_OPTIONS, arrayOf("One", "Two"))
            }
            PoslinkEntry.ACTION_SHOW_DIALOG_FORM -> {
                putString(EntryExtraData.PARAM_TITLE, "Form")
                putStringArray(EntryExtraData.PARAM_LABELS, arrayOf("A", "B"))
            }
            PoslinkEntry.ACTION_SHOW_SIGNATURE_BOX -> {
                putString(EntryExtraData.PARAM_TITLE, "Sign")
                putString(EntryExtraData.PARAM_TEXT, "Please sign")
                putInt(EntryExtraData.PARAM_SIGN_BOX, 0)
            }
            PoslinkEntry.ACTION_SHOW_INPUT_TEXT_BOX -> {
                putString(EntryExtraData.PARAM_TITLE, "Input box")
                putString(EntryExtraData.PARAM_MIN_LENGTH, "1")
                putString(EntryExtraData.PARAM_MAX_LENGTH, "10")
            }
            PoslinkEntry.ACTION_SHOW_TEXT_BOX -> {
                putString(EntryExtraData.PARAM_TITLE, "Text box")
                putString(EntryExtraData.PARAM_TEXT, "Body")
                putString(EntryExtraData.PARAM_BUTTON_1_NAME, "OK")
                putString(EntryExtraData.PARAM_BUTTON_1_KEY, "1")
            }
            else -> putString(EntryExtraData.PARAM_TITLE, "Poslink")
        }
    }

    private fun entrySmokeLabel(category: String, action: String): String = "$category::$action"

    private fun Bundle.fillSecurityCategoryBaseline() {
        putString(EntryExtraData.PARAM_MESSAGE, "")
        putString(EntryExtraData.PARAM_TITLE, "")
        putString(EntryExtraData.PARAM_PIN_STYLES, PinStyles.NORMAL)
        putBoolean(EntryExtraData.PARAM_IS_ONLINE_PIN, true)
        putString(EntryExtraData.PARAM_VCODE_NAME, "")
        putString(EntryExtraData.PARAM_PAN_STYLES, PanStyles.NORMAL)
        putString(EntryExtraData.PARAM_MERCHANT_NAME, "")
        putString(EntryExtraData.PARAM_ADMIN_PASSWORD_TYPE, AdminPasswordType.MANAGER)
    }

    private fun Bundle.fillOptionCategoryBaseline() {
        putString(EntryExtraData.PARAM_TITLE_MESSAGE, "")
        putString(EntryExtraData.PARAM_TITLE, "")
        putString(EntryExtraData.PARAM_MESSAGE, "")
    }

    private fun Bundle.fillConfirmationCategoryBaseline() {
        putString(EntryExtraData.PARAM_MESSAGE, "")
        putString(EntryExtraData.PARAM_CURRENCY, CurrencyType.USD)
        putLong(EntryExtraData.PARAM_TOTAL_AMOUNT, 0L)
        putLong(EntryExtraData.PARAM_APPROVED_AMOUNT, 0L)
        putLong(EntryExtraData.PARAM_BALANCE, 0L)
        putLong(EntryExtraData.PARAM_TIMEOUT, 30_000L)
        putBoolean(EntryExtraData.PARAM_CONFIRM_WITH_CURRENCY, false)
        putString(EntryExtraData.PARAM_AMOUNT_MESSAGE, "")
        putString(EntryExtraData.PARAM_EXCHANGE_RATE, "")
        putString(EntryExtraData.PARAM_CURRENCY_ALPHA_CODE, "")
        putString(EntryExtraData.PARAM_MARGIN, "")
        putString(EntryExtraData.PARAM_FOREIGN_AMOUNT_MESSAGE, "")
        putString(EntryExtraData.PARAM_PRINT_STATUS, "")
    }
}
