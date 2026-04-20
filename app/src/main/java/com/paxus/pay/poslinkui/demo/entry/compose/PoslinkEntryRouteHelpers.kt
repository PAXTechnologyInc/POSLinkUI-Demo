package com.paxus.pay.poslinkui.demo.entry.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFillAppearance
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkLegacyMaterialFilledButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButton
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkPrimaryButtonVariant
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkText
import com.paxus.pay.poslinkui.demo.ui.components.PosLinkTextRole
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
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
    val index: Int
)

internal fun resolvePoslinkTextBoxButtons(extras: Bundle): List<PoslinkTextBoxButtonSpec> {
    fun readName(index: Int): String {
        val constKey = when (index) {
            1 -> EntryExtraData.PARAM_BUTTON_1_NAME
            2 -> EntryExtraData.PARAM_BUTTON_2_NAME
            else -> EntryExtraData.PARAM_BUTTON_3_NAME
        }
        val fallbackKey = "button${index}Name"
        return extras.getString(constKey)?.takeIf { it.isNotBlank() }
            ?: extras.getString(fallbackKey).orEmpty()
    }
    fun readKey(index: Int): String {
        val constKey = when (index) {
            1 -> EntryExtraData.PARAM_BUTTON_1_KEY
            2 -> EntryExtraData.PARAM_BUTTON_2_KEY
            else -> EntryExtraData.PARAM_BUTTON_3_KEY
        }
        val fallbackKey = "button${index}Key"
        return extras.getString(constKey)?.takeIf { it.isNotBlank() }
            ?: extras.getString(fallbackKey).orEmpty()
    }
    return (1..3).mapNotNull { index ->
        val name = readName(index).trim()
        if (name.isBlank()) {
            null
        } else {
            PoslinkTextBoxButtonSpec(
                name = name,
                key = readKey(index).trim(),
                index = index
            )
        }
    }
}

@Composable
internal fun PoslinkTextBoxButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val fill = PosLinkDesignTokens.PrimaryColor
    PosLinkLegacyMaterialFilledButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        appearance = PosLinkLegacyMaterialFillAppearance(
            slotHeight = PosLinkDesignTokens.ButtonHeight,
            shape = RoundedCornerShape(PosLinkDesignTokens.LegacyButtonCornerRadius),
            containerColor = fill,
            disabledContainerColor = fill.copy(alpha = 0.38f),
            pressedContainerColor = PosLinkDesignTokens.LegacyButtonPressedColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFFECECEC))
        )
    }
}

internal fun normalizePoslinkTitleCommands(raw: String): String {
    if (raw.isBlank()) return raw
    // Some adb payloads escape title commands as \\L / \\R / \\B.
    // Collapse one-or-more leading slashes before known commands to a single slash
    // so legacy parser can consistently interpret alignment/weight/size markers.
    return raw.replace(Regex("""\\+([LRCB123n])"""), """\\$1""")
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
    val cleanedTitle = remember(rawTitle) {
        rawTitle
            .replace(Regex("""\\+[LRCB123n]"""), "")
            .trim()
    }
    if (cleanedTitle.isBlank()) return
    PosLinkText(
        text = cleanedTitle,
        role = PosLinkTextRole.ScreenTitle,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

internal fun containsShowTextBoxLineSepCommand(raw: String): Boolean {
    if (raw.isBlank()) return false
    return Regex("""\\+n""").containsMatchIn(raw)
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
 * 标题 + 正文（无图）：用于单 Scroll 或与 golive 一致时仅中间 List 区域滚动。
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
 * 图片 + imageDesc：golive 中位于 `ll_desc_msg_list_show_message`（ImageView + 横向 ll_desc_list）。
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
        // golive：图注走 getTitleViewList + customizeFontSize，无字号命令时为 FONT_NORMAL_SP(24sp)，单段居中
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
            // golive fragment_show_message：ListView 单独滚动；图片+desc 在 list 与 tax 之间固定，不随列表滚动
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
    // golive MessageItemAdapter：虽传 R.dimen.text_size_normal，但 getViewList→customizeFontize 默认 FONT_NORMAL_SP=24sp
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
    // fragment_show_message.xml：tax/total 均为 text_size_subtitle(18sp)，标签粗体
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
