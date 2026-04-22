package com.paxus.pay.poslinkui.demo.entry.compose

import android.annotation.SuppressLint
import android.graphics.Color as AndroidColor
import android.os.Bundle
import android.view.KeyEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.paxus.pay.poslinkui.demo.R
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImage
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkAsyncImageOptions
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import java.util.Locale
import org.json.JSONArray
import org.json.JSONObject

/**
 * Message resolution, bundle compat reads, and message display layout for POSLink routes.
 *
 * Title formatting and show-item JSON parsing: see `PoslinkEntryRouteTitleAndParsing.kt` in the same package.
 */
internal data class PoslinkTextBoxButtonSpec(
    val name: String,
    val key: String,
    val index: Int,
    val containerColor: Color? = null
)

internal fun resolvePoslinkTextBoxButtons(extras: Bundle): List<PoslinkTextBoxButtonSpec> {
    fun readValue(constKey: String, fallbackKey: String): String {
        return extras.get(constKey)?.toString()?.takeIf { it.isNotBlank() }
            ?: extras.get(fallbackKey)?.toString().orEmpty()
    }
    fun readName(index: Int): String {
        val constKey = when (index) {
            1 -> EntryExtraData.PARAM_BUTTON_1_NAME
            2 -> EntryExtraData.PARAM_BUTTON_2_NAME
            else -> EntryExtraData.PARAM_BUTTON_3_NAME
        }
        val fallbackKey = "button${index}Name"
        return readValue(constKey, fallbackKey)
    }
    fun readKey(index: Int): String {
        val constKey = when (index) {
            1 -> EntryExtraData.PARAM_BUTTON_1_KEY
            2 -> EntryExtraData.PARAM_BUTTON_2_KEY
            else -> EntryExtraData.PARAM_BUTTON_3_KEY
        }
        val fallbackKey = "button${index}Key"
        return readValue(constKey, fallbackKey)
    }
    fun readColor(index: Int): Color? {
        val constKey = when (index) {
            1 -> EntryExtraData.PARAM_BUTTON_1_COLOR
            2 -> EntryExtraData.PARAM_BUTTON_2_COLOR
            else -> EntryExtraData.PARAM_BUTTON_3_COLOR
        }
        val raw = extras.get(constKey)?.toString()
            ?: extras.get("button${index}Color")?.toString()
            ?: extras.get("PARAM_BUTTON_${index}_COLOR")?.toString()
        return parsePoslinkTextBoxButtonColor(raw)
    }
    return (1..3).mapNotNull { index ->
        val name = readName(index).trim()
        if (name.isBlank()) {
            null
        } else {
            PoslinkTextBoxButtonSpec(
                name = name,
                key = readKey(index).trim(),
                index = index,
                containerColor = readColor(index)
            )
        }
    }
}

internal fun parsePoslinkTextBoxButtonColor(raw: String?): Color? {
    val normalized = raw
        ?.trim()
        ?.removePrefix("#")
        ?.takeIf { it.length == 6 && it.all { ch -> ch.isDigit() || ch.lowercaseChar() in 'a'..'f' } }
        ?: return null
    return runCatching { Color(AndroidColor.parseColor("#$normalized")) }.getOrNull()
}

internal fun isPoslinkTextBoxHardKeyEnabled(extras: Bundle): Boolean {
    val raw = extras.get(EntryExtraData.PARAM_ENABLE_HARD_KEY)?.toString()
        ?: extras.get("enableHardKey")?.toString()
        ?: extras.get("PARAM_ENABLE_HARD_KEY")?.toString()
        ?: return false
    return raw.trim() == "1"
}

internal fun hasPoslinkTextBoxPhysicalKeyboard(extras: Bundle): Boolean {
    val raw = extras.get(EntryExtraData.PARAM_HAS_PHYSICAL_KEYBOARD)?.toString()
        ?: extras.get("hasPhyKeyboard")?.toString()
        ?: extras.get("PARAM_HAS_PHYSICAL_KEYBOARD")?.toString()
        ?: extras.get("hasPhysicalKeyboard")?.toString()
        ?: return false
    return raw.uppercase(Locale.ROOT) == "TRUE" || raw == "1"
}

internal fun shouldDisplayPoslinkTextBoxButtons(extras: Bundle): Boolean {
    return !isPoslinkTextBoxHardKeyEnabled(extras) || !hasPoslinkTextBoxPhysicalKeyboard(extras)
}

internal fun resolvePoslinkTextBoxHardKeyList(extras: Bundle): Set<String> {
    val raw = extras.get(EntryExtraData.PARAM_HARD_KEY_LIST)?.toString()
        ?: extras.get("hardKeyList")?.toString()
        ?: extras.get("PARAM_HARD_KEY_LIST")?.toString()
        ?: return emptySet()
    return raw.trim()
        .split(Regex("\\s+"))
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

internal fun resolvePoslinkTextBoxHardKeyResponse(
    buttons: List<PoslinkTextBoxButtonSpec>,
    hardKeyList: Set<String>,
    keyCode: Int
): String? {
    val keyName = when (keyCode) {
        KeyEvent.KEYCODE_0 -> "KEY0"
        KeyEvent.KEYCODE_1 -> "KEY1"
        KeyEvent.KEYCODE_2 -> "KEY2"
        KeyEvent.KEYCODE_3 -> "KEY3"
        KeyEvent.KEYCODE_4 -> "KEY4"
        KeyEvent.KEYCODE_5 -> "KEY5"
        KeyEvent.KEYCODE_6 -> "KEY6"
        KeyEvent.KEYCODE_7 -> "KEY7"
        KeyEvent.KEYCODE_8 -> "KEY8"
        KeyEvent.KEYCODE_9 -> "KEY9"
        KeyEvent.KEYCODE_ENTER -> "KEYENTER"
        KeyEvent.KEYCODE_BACK -> "KEYCANCEL"
        KeyEvent.KEYCODE_DEL -> "KEYCLEAR"
        else -> return null
    }
    if (hardKeyList.isNotEmpty() && keyName !in hardKeyList) return null
    return buttons.firstOrNull { it.key.equals(keyName, ignoreCase = true) }?.key?.ifBlank { keyName }
}

internal fun resolvePoslinkTextBoxButtonRows(
    buttons: List<PoslinkTextBoxButtonSpec>
): List<List<PoslinkTextBoxButtonSpec>> = when (buttons.size) {
    1, 2, 3 -> listOf(buttons)
    else -> buttons.map(::listOf)
}

@Composable
internal fun PoslinkTextBoxButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = PosLinkDesignTokens.PrimaryColor
) {
    val buttonMargin = dimensionResource(R.dimen.margin_gap)
    val cells = remember(text) {
        buildPoslinkValueLikeCells(text)
            .map { it.copy(align = TextAlign.Center) }
    }
    Surface(
        onClick = onClick,
        modifier = modifier
            .padding(vertical = buttonMargin)
            .height(dimensionResource(R.dimen.button_height)),
        enabled = enabled,
        shape = RectangleShape,
        color = if (enabled) containerColor else PosLinkDesignTokens.DisabledColor,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PoslinkLegacyInlineTextContent(
                cells = cells,
                allowWrap = true,
                textColor = if (enabled) {
                    PosLinkDesignTokens.PrimaryActionTextColor
                } else {
                    PosLinkDesignTokens.PrimaryActionTextColor.copy(alpha = 0.38f)
                }
            )
        }
    }
}

internal fun normalizePoslinkTitleCommands(raw: String): String {
    return normalizePoslinkControlCommands(raw)
}

internal fun shouldRenderShowTextBoxCenterTitle(
    normalizedTitle: String,
    normalizedBody: String,
    buttonCount: Int
): Boolean {
    if (buttonCount != 3) return false
    if (normalizedBody.isNotBlank()) return false
    val hasCenter = normalizedTitle.contains("\\C")
    val hasOtherAlign = normalizedTitle.contains("\\L") || normalizedTitle.contains("\\R")
    return hasCenter && !hasOtherAlign
}

@Composable
internal fun PoslinkCenteredTextBoxTitle(rawTitle: String) {
    PoslinkLegacyTitleLikeText(raw = rawTitle, supportLineSep = true)
}

internal fun containsShowTextBoxLineSepCommand(raw: String): Boolean {
    if (raw.isBlank()) return false
    return Regex("""\\+n""").containsMatchIn(normalizePoslinkControlCommands(raw))
}

internal fun parseShowDialogFormCheckedIndexes(labelsProperty: String?, labelsSize: Int): Set<Int> {
    if (labelsProperty.isNullOrBlank() || labelsSize <= 0) return emptySet()
    return labelsProperty
        .split(',')
        .mapIndexedNotNull { index, value ->
            if (index >= labelsSize) {
                null
            } else if (value.trim() == "1") {
                index
            } else {
                null
            }
        }
        .toSet()
}

internal data class PoslinkDisplayImage(
    val url: String = "",
    val desc: String = ""
)

internal enum class PoslinkMessageVisualMode {
    Default,
    ShowMessageLegacy,
    ShowItemLegacy
}

internal data class PoslinkMessageFallbackText(
    val text: String,
    val source: String
)

internal val SHOW_MESSAGE_FALLBACK_TEXT_KEYS = listOf(
    "text",
    "message",
    "body",
    "content",
    "msg",
    "description"
)

internal fun resolvePoslinkShowMessageRaw(extras: Bundle): String {
    val primary = extras.readCompatValueAsString(EntryExtraData.PARAM_MESSAGE_LIST)
    if (primary.isNotBlank()) return primary
    val fallbackKeys = listOf(
        "PARAM_MESSAGE_LIST",
        "paramMessageList",
        "messageList",
        "msgList",
        "messages"
    )
    for (key in fallbackKeys) {
        val value = extras.readCompatValueAsString(key)
        if (value.isNotBlank()) return value
    }
    return ""
}

internal fun resolvePoslinkShowMessageFallbackText(raw: String): PoslinkMessageFallbackText? {
    if (raw.isBlank()) return null
    val normalizedRaw = normalizePoslinkPayload(raw)
    val jsonFallback = runCatching {
        val content = normalizedRaw.trim()
        when {
            content.startsWith("{") -> JSONObject(content).findShowMessageFallbackText("root")
            content.startsWith("[") -> JSONArray(content).findShowMessageFallbackText("root")
            else -> null
        }
    }.getOrNull()
    if (jsonFallback != null) return jsonFallback
    SHOW_MESSAGE_FALLBACK_TEXT_KEYS.forEach { key ->
        extractLooseField(normalizedRaw, key)
            ?.takeIf { it.isNotBlank() }
            ?.let { return PoslinkMessageFallbackText(it, "loose:$key") }
    }
    return normalizedRaw
        .trim()
        .takeIf { it.isNotBlank() && !it.startsWith("{") && !it.startsWith("[") }
        ?.let { PoslinkMessageFallbackText(it, "raw") }
}

internal fun JSONObject.findShowMessageFallbackText(path: String): PoslinkMessageFallbackText? {
    SHOW_MESSAGE_FALLBACK_TEXT_KEYS.forEach { key ->
        optStringOrNull(key)
            ?.takeIf { it.isNotBlank() }
            ?.let { return PoslinkMessageFallbackText(it, "$path.$key") }
    }
    val iter = keys()
    while (iter.hasNext()) {
        val key = iter.next()
        when (val value = opt(key)) {
            is JSONObject -> value.findShowMessageFallbackText("$path.$key")?.let { return it }
            is JSONArray -> value.findShowMessageFallbackText("$path.$key")?.let { return it }
            is String -> {
                val trimmed = value.trim()
                if (trimmed.isNotBlank() && (key.equals("text", true) || key.equals("message", true))) {
                    return PoslinkMessageFallbackText(trimmed, "$path.$key")
                }
            }
        }
    }
    return null
}

internal fun JSONArray.findShowMessageFallbackText(path: String): PoslinkMessageFallbackText? {
    for (index in 0 until length()) {
        when (val item = opt(index)) {
            is JSONObject -> item.findShowMessageFallbackText("$path[$index]")?.let { return it }
            is JSONArray -> item.findShowMessageFallbackText("$path[$index]")?.let { return it }
            is String -> {
                val trimmed = item.trim()
                if (trimmed.isNotBlank() && !trimmed.startsWith("{") && !trimmed.startsWith("[")) {
                    return PoslinkMessageFallbackText(trimmed, "$path[$index]")
                }
            }
        }
    }
    return null
}

/**
 * 鏍囬 + 姝ｆ枃锛堟棤鍥撅級锛氱敤浜庡崟 Scroll 鎴栦笌 golive 涓€鑷存椂浠呬腑闂?List 鍖哄煙婊氬姩銆?
 */
@Composable
internal fun PoslinkMessageTitleAndMessages(
    title: String,
    messageText: String,
    visualMode: PoslinkMessageVisualMode,
    leadingSpacingBeforeMessage: Boolean = true
) {
    val useLegacyTitleLayout = visualMode != PoslinkMessageVisualMode.Default
    val useLegacyMessageGroups = visualMode == PoslinkMessageVisualMode.ShowMessageLegacy
    if (title.isNotBlank()) {
        when {
            visualMode == PoslinkMessageVisualMode.ShowMessageLegacy && !title.contains('\\') -> {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = PosLinkDesignTokens.PoslinkTextShowingNormalSp,
                        lineHeight = PosLinkDesignTokens.PoslinkTextShowingNormalLineHeight,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )
            }
            visualMode == PoslinkMessageVisualMode.ShowItemLegacy && !title.contains('\\') -> {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )
            }
            useLegacyTitleLayout -> PoslinkFormattedTitleLegacy(title = title)
            else -> PoslinkFormattedTitle(title = title)
        }
    }
    if (messageText.isNotBlank()) {
        if (leadingSpacingBeforeMessage && title.isNotBlank()) {
            Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        }
        if (useLegacyMessageGroups) {
            PoslinkMessageListText(messageText = messageText)
        } else {
            PosLinkText(text = messageText, role = PosLinkTextRole.Supporting)
        }
    }
}

/**
 * 鍥剧墖 + imageDesc锛歡olive 涓綅浜?`ll_desc_msg_list_show_message`锛圛mageView + 妯悜 ll_desc_list锛夈€?
 */
@Composable
internal fun PoslinkMessageImageDescBlock(
    imageUrl: String,
    imageDesc: String,
    useLegacyImageBounds: Boolean
) {
    if (imageUrl.isBlank()) return
    PosLinkAsyncImage(
        data = imageUrl,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (useLegacyImageBounds) 150.dp else 120.dp),
        options = PosLinkAsyncImageOptions(
            contentDescription = imageDesc.ifBlank { null },
            contentScale = if (useLegacyImageBounds) ContentScale.FillBounds else ContentScale.Crop
        )
    )
    if (imageDesc.isNotBlank()) {
        Spacer(Modifier.height(5.dp))
        // golive锛氬浘娉ㄨ蛋 getTitleViewList + customizeFontSize锛屾棤瀛楀彿鍛戒护鏃朵负 FONT_NORMAL_SP(24sp)锛屽崟娈靛眳涓?
        Text(
            text = imageDesc,
            modifier = Modifier.fillMaxWidth(),
            color = PosLinkDesignTokens.PrimaryTextColor,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = PosLinkDesignTokens.PoslinkTextShowingNormalSp,
                lineHeight = PosLinkDesignTokens.PoslinkTextShowingNormalLineHeight,
                fontWeight = FontWeight.Normal
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun PoslinkMessageDisplayScrollColumn(
    title: String,
    messageText: String,
    imageUrl: String,
    imageDesc: String,
    visualMode: PoslinkMessageVisualMode
) {
    val useLegacyImageBounds = visualMode != PoslinkMessageVisualMode.Default
    PoslinkMessageTitleAndMessages(title = title, messageText = messageText, visualMode = visualMode)
    if (imageUrl.isNotBlank()) {
        Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
        PoslinkMessageImageDescBlock(
            imageUrl = imageUrl,
            imageDesc = imageDesc,
            useLegacyImageBounds = useLegacyImageBounds
        )
    }
}

@Composable
internal fun PoslinkMessageDisplayFooterColumn(
    tax: String,
    total: String,
    showConfirmButton: Boolean,
    visualMode: PoslinkMessageVisualMode,
    onConfirm: () -> Unit
) {
    val useLegacyTaxTotalStyle = visualMode != PoslinkMessageVisualMode.Default
    PoslinkTaxTotalFooter(
        tax = tax,
        total = total,
        showMessageLegacy = useLegacyTaxTotalStyle
    )
    if (showConfirmButton) {
        Spacer(Modifier.height(PosLinkDesignTokens.SectionBreakSpacing))
        PosLinkPrimaryButton(
            text = stringResource(R.string.confirm),
            onClick = onConfirm,
            variant = PosLinkPrimaryButtonVariant.PoslinkLegacy
        )
    }
}

/**
 * Scrollable message body (title, text, optional image) for show-message / show-item layouts.
 */
internal data class PoslinkMessageDisplayBodyParams(
    val title: String,
    val messageText: String,
    val imageUrl: String,
    val imageDesc: String
)

/**
 * Tax/total strip and confirm affordance for [PoslinkMessageDisplayLayout].
 */
internal data class PoslinkMessageDisplayFooterParams(
    val tax: String,
    val total: String,
    val visualMode: PoslinkMessageVisualMode = PoslinkMessageVisualMode.Default,
    val showConfirmButton: Boolean = true,
    val onConfirm: () -> Unit
)

internal data class PoslinkMessageDisplayLayoutParams(
    val body: PoslinkMessageDisplayBodyParams,
    val footer: PoslinkMessageDisplayFooterParams
)

@Composable
internal fun PoslinkMessageDisplayLayout(p: PoslinkMessageDisplayLayoutParams) {
    val title = p.body.title
    val messageText = p.body.messageText
    val imageUrl = p.body.imageUrl
    val imageDesc = p.body.imageDesc
    val tax = p.footer.tax
    val total = p.footer.total
    val visualMode = p.footer.visualMode
    val showConfirmButton = p.footer.showConfirmButton
    val onConfirm = p.footer.onConfirm
    val bottomInset = if (showConfirmButton) 140.dp else 92.dp
    val useLegacyImageBounds = visualMode != PoslinkMessageVisualMode.Default
    Box(modifier = Modifier.fillMaxSize()) {
        if (visualMode == PoslinkMessageVisualMode.ShowMessageLegacy) {
            // golive fragment_show_message锛歀istView 鍗曠嫭婊氬姩锛涘浘鐗?desc 鍦?list 涓?tax 涔嬮棿鍥哄畾锛屼笉闅忓垪琛ㄦ粴鍔?
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomInset)
                    .padding(5.dp)
            ) {
                if (title.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        PoslinkMessageTitleAndMessages(
                            title = title,
                            messageText = "",
                            visualMode = visualMode,
                            leadingSpacingBeforeMessage = false
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (messageText.isNotBlank()) {
                        PoslinkMessageTitleAndMessages(
                            title = "",
                            messageText = messageText,
                            visualMode = visualMode,
                            leadingSpacingBeforeMessage = false
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                if (imageUrl.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp)
                    ) {
                        PoslinkMessageImageDescBlock(
                            imageUrl = imageUrl,
                            imageDesc = imageDesc,
                            useLegacyImageBounds = useLegacyImageBounds
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(bottom = bottomInset)
            ) {
                PoslinkMessageDisplayScrollColumn(
                    title = title,
                    messageText = messageText,
                    imageUrl = imageUrl,
                    imageDesc = imageDesc,
                    visualMode = visualMode
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            PoslinkMessageDisplayFooterColumn(
                tax = tax,
                total = total,
                showConfirmButton = showConfirmButton,
                visualMode = visualMode,
                onConfirm = onConfirm
            )
        }
    }
}

@Composable
internal fun PoslinkMessageListText(messageText: String) {
    val groups = remember(messageText) { parsePoslinkMessageGroups(messageText) }
    if (groups.isEmpty()) return
    // golive MessageItemAdapter锛氳櫧浼?R.dimen.text_size_normal锛屼絾 getViewList鈫抍ustomizeFontize 榛樿 FONT_NORMAL_SP=24sp
    val lineStyle = MaterialTheme.typography.bodyLarge.copy(
        fontWeight = FontWeight.Normal,
        fontSize = PosLinkDesignTokens.PoslinkTextShowingNormalSp,
        lineHeight = PosLinkDesignTokens.PoslinkTextShowingNormalLineHeight
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        groups.forEachIndexed { index, group ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = group.msg1,
                    modifier = Modifier.fillMaxWidth(),
                    color = PosLinkDesignTokens.PrimaryTextColor,
                    style = lineStyle
                )
                group.msg2?.let { second ->
                    Spacer(Modifier.height(PosLinkDesignTokens.MicroSpacing))
                    Text(
                        text = second,
                        modifier = Modifier.fillMaxWidth(),
                        color = PosLinkDesignTokens.PrimaryTextColor,
                        style = lineStyle
                    )
                }
            }
            if (index != groups.lastIndex) {
                Spacer(Modifier.height(PosLinkDesignTokens.SpaceBetweenTextView))
            }
        }
    }
}

internal data class PoslinkMessageGroup(
    val msg1: String,
    val msg2: String?
)

internal fun parsePoslinkMessageGroups(messageText: String): List<PoslinkMessageGroup> {
    val lines = messageText.lines()
        .map { it.trimEnd() }
        .filter { it.isNotBlank() }
    if (lines.isEmpty()) return emptyList()
    return lines.chunked(2).mapNotNull { block ->
        val first = block.firstOrNull().orEmpty().trim()
        if (first.isBlank()) {
            null
        } else {
            PoslinkMessageGroup(
                msg1 = first,
                msg2 = block.getOrNull(1)?.trim()?.takeIf { it.isNotBlank() }
            )
        }
    }
}

@Composable
internal fun PoslinkTaxTotalFooter(
    tax: String,
    total: String,
    showMessageLegacy: Boolean = false
) {
    if (tax.isBlank() && total.isBlank()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (showMessageLegacy) Modifier.padding(horizontal = 5.dp) else Modifier
            )
    ) {
        if (tax.isNotBlank()) {
            PoslinkTaxTotalRow(
                label = stringResource(R.string.detail_item_tax),
                value = tax,
                showMessageLegacy = showMessageLegacy
            )
        }
        if (total.isNotBlank()) {
            Spacer(Modifier.height(PosLinkDesignTokens.CompactSpacing))
            PoslinkTaxTotalRow(
                label = stringResource(R.string.pete_total),
                value = total,
                showMessageLegacy = showMessageLegacy
            )
        }
    }
}

@Composable
internal fun PoslinkTaxTotalRow(
    label: String,
    value: String,
    showMessageLegacy: Boolean = false
) {
    // fragment_show_message.xml锛歵ax/total 鍧囦负 text_size_subtitle(18sp)锛屾爣绛剧矖浣?
    if (showMessageLegacy) {
        val labelStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        val valueStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = PosLinkDesignTokens.PrimaryTextColor,
                style = labelStyle,
                textAlign = TextAlign.Start
            )
            Text(
                text = value,
                color = PosLinkDesignTokens.PrimaryTextColor,
                style = valueStyle,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth()) {
            PosLinkText(
                text = label,
                role = PosLinkTextRole.Supporting,
                modifier = Modifier.weight(1f)
            )
            PosLinkText(
                text = value,
                role = PosLinkTextRole.Body,
                textAlign = TextAlign.End
            )
        }
    }
}

internal fun resolvePoslinkDisplayImage(
    extras: Bundle,
    fallbackRawPayload: String
): PoslinkDisplayImage {
    val imageUrl = resolveFirstNonBlankString(
        extras,
        keys = listOf("PARAM_IMAGE_URL", "paramImageUrl", "imageURL", "imageUrl", "productImgUri")
    )
    val imageDesc = resolveFirstNonBlankString(
        extras,
        keys = listOf("PARAM_IMAGE_DESC", "paramImageDesc", "imageDesc", "productImgDesc")
    )
    if (imageUrl.isNotBlank() || imageDesc.isNotBlank()) {
        return PoslinkDisplayImage(url = imageUrl, desc = imageDesc)
    }
    val fromPayload = resolvePoslinkImageFromPayload(fallbackRawPayload)
    return PoslinkDisplayImage(
        url = fromPayload.first,
        desc = fromPayload.second
    )
}

@SuppressLint("DiscouragedApi")
internal fun resolveShowMessageDrawableUri(activity: FragmentActivity, imageDesc: String): String {
    val normalizedName = imageDesc
        .trim()
        .substringAfterLast('/')
        .substringBeforeLast('.')
        .trim()
    if (normalizedName.isBlank()) return ""
    // Drawable name comes from host payload at runtime; R.drawable.* cannot be resolved statically.
    val resId = activity.resources.getIdentifier(normalizedName, "drawable", activity.packageName)
    return if (resId != 0) {
        "android.resource://${activity.packageName}/$resId"
    } else {
        ""
    }
}

internal fun resolveFirstNonBlankString(extras: Bundle, keys: List<String>): String {
    keys.forEach { key ->
        val value = extras.readCompatValueAsString(key).trim()
        if (value.isNotBlank()) return value
    }
    return ""
}

internal fun Bundle.readCompatValueAsString(key: String): String {
    if (!containsKey(key)) return ""
    return get(key).toCompatPayloadString().trim()
}

internal fun Bundle.readCompatInt(keys: List<String>, defaultValue: Int): Int {
    keys.forEach { key ->
        if (!containsKey(key)) return@forEach
        when (val value = get(key)) {
            is Number -> return value.toInt()
            is String -> value.trim().toIntOrNull()?.let { return it }
        }
    }
    return defaultValue
}

internal fun Bundle.readCompatLong(keys: List<String>, defaultValue: Long): Long {
    keys.forEach { key ->
        if (!containsKey(key)) return@forEach
        when (val value = get(key)) {
            is Number -> return value.toLong()
            is String -> value.trim().toLongOrNull()?.let { return it }
        }
    }
    return defaultValue
}

internal fun Any?.toCompatPayloadString(): String = when (this) {
    null -> ""
    is String -> this
    is CharSequence -> this.toString()
    is Bundle -> this.keySet().joinToString(prefix = "{", postfix = "}") { nestedKey ->
        "$nestedKey=${this.get(nestedKey).toCompatPayloadString()}"
    }
    is Map<*, *> -> this.entries.joinToString(prefix = "{", postfix = "}") { entry ->
        "${entry.key}=${entry.value.toCompatPayloadString()}"
    }
    is Iterable<*> -> this.joinToString(prefix = "[", postfix = "]") { it.toCompatPayloadString() }
    is Array<*> -> this.joinToString(prefix = "[", postfix = "]") { it.toCompatPayloadString() }
    else -> this.toString()
}

internal fun resolvePoslinkImageFromPayload(raw: String): Pair<String, String> {
    if (raw.isBlank()) return "" to ""
    val url = extractLooseField(raw, "imageURL")
        ?: extractLooseField(raw, "imageUrl")
        ?: extractLooseField(raw, "productImgUri")
        ?: ""
    val desc = extractLooseField(raw, "imageDesc")
        ?: extractLooseField(raw, "productImgDesc")
        ?: ""
    return url.trim() to desc.trim()
}
