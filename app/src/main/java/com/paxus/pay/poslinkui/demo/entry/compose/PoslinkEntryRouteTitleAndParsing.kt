package com.paxus.pay.poslinkui.demo.entry.compose

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.pax.us.pay.ui.constant.entry.EntryExtraData
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import java.math.BigDecimal
import java.math.RoundingMode
import org.json.JSONArray
import org.json.JSONObject

/**
 * Title formatting composables and show-item / message-list JSON parsing for POSLink routes.
 *
 * Kept separate from `PoslinkEntryRouteHelpers.kt` to satisfy the Sonar Kotlin file line-count rule.
 */
internal data class PoslinkTitleSegment(
    val text: String,
    val align: TextAlign,
    val bold: Boolean,
    val sizeLevel: Int
)

internal data class PoslinkLegacyTitleLine(
    val left: PoslinkTitleSegment? = null,
    val center: PoslinkTitleSegment? = null,
    val right: PoslinkTitleSegment? = null
)

@Composable
internal fun PoslinkFormattedTitle(title: String) {
    val segments = remember(title) { parsePoslinkTitleSegments(title) }
    segments.forEach { segment ->
        Text(
            text = segment.text,
            modifier = Modifier.fillMaxWidth(),
            color = PosLinkDesignTokens.PrimaryTextColor,
            textAlign = segment.align,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = if (segment.bold) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = resolvePoslinkTitleFontSize(segment.sizeLevel)
            )
        )
    }
}

@Composable
internal fun PoslinkFormattedTitleLegacy(title: String) {
    val lines = remember(title) { parsePoslinkTitleLegacyLines(title) }
    if (lines.isEmpty()) return
    Column(modifier = Modifier.fillMaxWidth()) {
        lines.forEach { line ->
            Box(modifier = Modifier.fillMaxWidth()) {
                line.left?.let { segment ->
                    PoslinkLegacyTitleText(
                        segment = segment,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
                line.center?.let { segment ->
                    PoslinkLegacyTitleText(
                        segment = segment,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                line.right?.let { segment ->
                    PoslinkLegacyTitleText(
                        segment = segment,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
    }
}

@Composable
internal fun PoslinkFormattedTextBoxTitle(title: String) {
    if (shouldUseLegacyShowTextBoxSlotLayout(title)) {
        PoslinkFormattedTextBoxSlotTitle(title = title)
    } else {
        PoslinkFormattedTitleLegacy(title = title)
    }
}

internal fun shouldUseLegacyShowTextBoxSlotLayout(title: String): Boolean {
    val hasExplicitAlign = title.contains("\\L") || title.contains("\\R") || title.contains("\\C")
    val hasNumericCommand = title.contains("\\1") || title.contains("\\2") || title.contains("\\3")
    return !hasExplicitAlign && hasNumericCommand
}

@Composable
internal fun PoslinkFormattedTextBoxSlotTitle(title: String) {
    val segments = remember(title) {
        parsePoslinkTitleSegments(title)
            .map { it.copy(text = it.text.trim()) }
            .filter { it.text.isNotBlank() }
    }
    if (segments.isEmpty()) return
    val ordered = remember(segments) { reorderLegacyTitleSegments(segments) }
    Row(modifier = Modifier.fillMaxWidth()) {
        ordered.forEachIndexed { index, segment ->
            val align = when (index) {
                1 -> Alignment.Center
                else -> Alignment.CenterStart
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = PosLinkDesignTokens.CompactSpacing + PosLinkDesignTokens.MicroSpacing),
                contentAlignment = align
            ) {
                PoslinkLegacyTitleText(
                    segment = segment,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

internal fun reorderLegacyTitleSegments(
    segments: List<PoslinkTitleSegment>
): List<PoslinkTitleSegment> {
    if (segments.size <= 1) return segments
    val center = segments.first()
    val others = segments.drop(1).toMutableList()
    val insertAt = (others.size + 1) / 2
    others.add(insertAt, center)
    return others
}

@Composable
internal fun PoslinkLegacyTitleText(segment: PoslinkTitleSegment, modifier: Modifier) {
    Text(
        text = segment.text,
        modifier = modifier,
        color = PosLinkDesignTokens.PrimaryTextColor,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = if (segment.bold) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = resolvePoslinkTitleFontSize(segment.sizeLevel)
        )
    )
}

internal fun resolvePoslinkTitleFontSize(sizeLevel: Int): TextUnit = when (sizeLevel) {
    1 -> PosLinkDesignTokens.SupportingTextSize
    3 -> PosLinkDesignTokens.DisplayTextSize
    else -> PosLinkDesignTokens.TitleTextSize
}

private fun textAlignForPoslinkTitleCommand(command: Char): TextAlign = when (command) {
    'R' -> TextAlign.Right
    'C' -> TextAlign.Center
    else -> TextAlign.Left
}

private data class PoslinkTitleParseStyle(
    val align: TextAlign = TextAlign.Left,
    val bold: Boolean = false,
    val sizeLevel: Int = 2
)

internal fun parsePoslinkTitleSegments(raw: String): List<PoslinkTitleSegment> {
    val parsed = mutableListOf<PoslinkTitleSegment>()
    val builder = StringBuilder()
    var style = PoslinkTitleParseStyle()

    fun flush() {
        if (builder.isEmpty()) return
        parsed += PoslinkTitleSegment(
            text = builder.toString(),
            align = style.align,
            bold = style.bold,
            sizeLevel = style.sizeLevel
        )
        builder.clear()
    }

    var index = 0
    while (index < raw.length) {
        val current = raw[index]
        if (current == '\\' && index + 1 < raw.length) {
            val cmd = raw[index + 1]
            when (cmd) {
                'L', 'R', 'C' -> {
                    flush()
                    style = style.copy(align = textAlignForPoslinkTitleCommand(cmd))
                    index += 2
                    continue
                }

                'B' -> {
                    flush()
                    style = style.copy(bold = true)
                    index += 2
                    continue
                }

                '1', '2', '3' -> {
                    flush()
                    style = style.copy(sizeLevel = cmd.digitToInt())
                    index += 2
                    continue
                }

                'n' -> {
                    flush()
                    index += 2
                    continue
                }
            }
        }
        builder.append(current)
        index++
    }
    flush()
    return parsed
}

/**
 * Collects [PoslinkLegacyTitleLine] rows while scanning title segments (legacy \\L/\\R/\\C alignment).
 */
internal class PoslinkLegacyTitleLineAssembler(
    private val hasExplicitAlignment: Boolean
) {
    private val lines = mutableListOf<PoslinkLegacyTitleLine>()
    private var currentLeft: PoslinkTitleSegment? = null
    private var currentCenter: PoslinkTitleSegment? = null
    private var currentRight: PoslinkTitleSegment? = null

    private fun flushLine() {
        if (currentLeft == null && currentCenter == null && currentRight == null) return
        lines += PoslinkLegacyTitleLine(
            left = currentLeft,
            center = currentCenter,
            right = currentRight
        )
        currentLeft = null
        currentCenter = null
        currentRight = null
    }

    fun acceptPart(part: PoslinkTitleSegment) {
        when (part.align) {
            TextAlign.Left -> {
                if (currentLeft != null) flushLine()
                currentLeft = part
            }
            TextAlign.Right -> {
                if (currentRight != null) flushLine()
                currentRight = part
            }
            else -> {
                if (currentCenter != null) flushLine()
                currentCenter = part
            }
        }
        if (currentLeft != null && currentCenter != null && currentRight != null) {
            flushLine()
        }
    }

    fun ingestSegments(segments: List<PoslinkTitleSegment>) {
        segments.forEach { segment ->
            val normalizedParts = segment.text.split('\n')
                .map { part ->
                    val adjustedAlign = if (hasExplicitAlignment) segment.align else TextAlign.Center
                    segment.copy(text = part.trim(), align = adjustedAlign)
                }
                .filter { part -> part.text.isNotBlank() }
            normalizedParts.forEach { part -> acceptPart(part) }
        }
    }

    fun finish(): List<PoslinkLegacyTitleLine> {
        flushLine()
        return lines
    }
}

internal fun parsePoslinkTitleLegacyLines(raw: String): List<PoslinkLegacyTitleLine> {
    val segments = parsePoslinkTitleSegments(raw)
    if (segments.isEmpty()) return emptyList()
    val hasExplicitAlignment = raw.contains("\\L") || raw.contains("\\R") || raw.contains("\\C")
    return PoslinkLegacyTitleLineAssembler(hasExplicitAlignment)
        .also { it.ingestSegments(segments) }
        .finish()
}

internal fun parsePoslinkMessageList(raw: String): String {
    if (raw.isBlank()) return ""
    val normalizedRaw = normalizePoslinkPayload(raw)
    val strictLines = parsePoslinkMessageListFromJson(normalizedRaw)
    if (strictLines.isNotEmpty()) return strictLines.joinToString("\n")
    val looseLines = parsePoslinkMessageListLoose(normalizedRaw)
    return looseLines.joinToString("\n")
}

internal fun parsePoslinkMessageListFromJson(raw: String): List<String> = runCatching {
    val content = raw.trim()
    when {
        content.startsWith("[") -> JSONArray(content).extractPoslinkMessageLines()
        content.startsWith("{") -> JSONObject(content).extractPoslinkMessageLinesFromObject()
        else -> emptyList()
    }
}.getOrDefault(emptyList())

internal fun parsePoslinkMessageListLoose(raw: String): List<String> {
    val lines = mutableListOf<String>()
    val objectBlocks = Regex("\\{[^{}]*\\}").findAll(raw).map { it.value }.toList()
    if (objectBlocks.isNotEmpty()) {
        objectBlocks.forEach { block ->
            buildPoslinkMessageLine(
                msg1 = extractLooseField(block, "msg1") ?: extractLooseField(block, "message1"),
                msg2 = extractLooseField(block, "msg2") ?: extractLooseField(block, "message2")
            )?.let(lines::add)
        }
        if (lines.isNotEmpty()) return lines
    }
    buildPoslinkMessageLine(
        msg1 = extractLooseField(raw, "msg1") ?: extractLooseField(raw, "message1"),
        msg2 = extractLooseField(raw, "msg2") ?: extractLooseField(raw, "message2")
    )?.let(lines::add)
    return lines
}

internal fun JSONArray.extractPoslinkMessageLines(): List<String> {
    val lines = mutableListOf<String>()
    for (index in 0 until length()) {
        val item = opt(index)
        when (item) {
            is JSONObject -> item.toPoslinkMessageLine()?.let(lines::add)
            is String -> parsePoslinkMessageList(item).takeIf { it.isNotBlank() }?.let(lines::add)
        }
    }
    return lines
}

internal fun JSONObject.toPoslinkMessageLine(): String? {
    val nestedMsgInfo = when {
        has("MsgInfo") -> optJSONObject("MsgInfo")
        has("msgInfo") -> optJSONObject("msgInfo")
        has("message") -> optJSONObject("message")
        else -> null
    }
    if (nestedMsgInfo == null) {
        val nestedRaw = optStringOrNull("MsgInfo")
            ?: optStringOrNull("msgInfo")
            ?: optStringOrNull("message")
        if (!nestedRaw.isNullOrBlank()) {
            val normalizedNested = normalizePoslinkPayload(nestedRaw)
            runCatching { JSONObject(normalizedNested) }
                .getOrNull()
                ?.toPoslinkMessageLine()
                ?.let { return it }
            buildPoslinkMessageLine(
                msg1 = extractLooseField(normalizedNested, "msg1")
                    ?: extractLooseField(normalizedNested, "message1"),
                msg2 = extractLooseField(normalizedNested, "msg2")
                    ?: extractLooseField(normalizedNested, "message2")
            )?.let { return it }
        }
    }
    val source = nestedMsgInfo ?: this
    return buildPoslinkMessageLine(
        msg1 = source.optStringOrNull("msg1") ?: source.optStringOrNull("message1"),
        msg2 = source.optStringOrNull("msg2") ?: source.optStringOrNull("message2")
    )
}

internal fun buildPoslinkMessageLine(msg1: String?, msg2: String?): String? {
    val line1 = msg1?.trim().orEmpty()
    val line2 = msg2?.trim().orEmpty()
    return listOf(line1, line2)
        .filter { it.isNotBlank() }
        .takeIf { it.isNotEmpty() }
        ?.joinToString("\n")
}

internal fun extractLooseField(raw: String, key: String): String? {
    val pattern = Regex(
        """(?i)["']?$key["']?\s*[:=]\s*(?:"([^"]*)"|'([^']*)'|([^,\}\]]+))"""
    )
    val match = pattern.find(raw) ?: return null
    return match.groups[1]?.value
        ?: match.groups[2]?.value
        ?: match.groups[3]?.value?.trim()
}

internal data class PoslinkItemDetail(
    val productName: String? = null,
    val pluCode: String? = null,
    val price: String? = null,
    val unit: String? = null,
    val unitPrice: String? = null,
    val tax: String? = null,
    val quantity: String? = null,
    val productImgUri: String? = null,
    val productImgDesc: String? = null
)

internal fun resolvePoslinkShowItemRaw(extras: Bundle): String {
    val primary = extras.readCompatValueAsString(EntryExtraData.PARAM_MESSAGE_LIST)
    if (primary.isNotBlank()) return primary
    val fallbackKeys = listOf("PARAM_ITEMS", "paramItems", "items", "itemList", "messageList")
    for (key in fallbackKeys) {
        val value = extras.readCompatValueAsString(key)
        if (value.isNotBlank()) return value
    }
    return ""
}

internal fun isPoslinkOuterQuoteWrapper(s: String): Boolean {
    if (s.length <= 1) return false
    val doubleQuoted = s.startsWith('"') && s.endsWith('"')
    val singleQuoted = s.startsWith('\'') && s.endsWith('\'')
    return doubleQuoted || singleQuoted
}

internal fun poslinkInnerLooksLikeJsonStructure(inner: String): Boolean {
    if (inner.startsWith("{") || inner.startsWith("[")) return true
    return inner.startsWith("\\{") || inner.startsWith("\\[")
}

internal fun poslinkJsonDoubleQuotedEnvelope(s: String): Boolean {
    val wrappedObject = s.startsWith("\"{") && s.endsWith("}\"")
    val wrappedArray = s.startsWith("\"[") && s.endsWith("]\"")
    return wrappedObject || wrappedArray
}

internal fun parsePoslinkShowItemList(raw: String, currencySymbol: String): String {
    if (raw.isBlank()) return ""
    val normalizedRaw = normalizePoslinkPayload(raw)
    val strictItems = parsePoslinkShowItemListFromJson(normalizedRaw)
    if (strictItems.isNotEmpty()) return strictItems.toReadablePoslinkItemText(currencySymbol)
    val looseItems = parsePoslinkShowItemListLoose(normalizedRaw)
    if (looseItems.isNotEmpty()) return looseItems.toReadablePoslinkItemText(currencySymbol)
    val fallback = parsePoslinkMessageList(normalizedRaw)
    return fallback.ifBlank { "Item details unavailable" }
}

internal fun normalizePoslinkPayload(raw: String): String {
    if (raw.isBlank()) return ""
    var s = raw.trim()
    if (isPoslinkOuterQuoteWrapper(s)) {
        val inner = s.substring(1, s.length - 1).trim()
        if (poslinkInnerLooksLikeJsonStructure(inner)) {
            s = inner
        }
    }
    while (s.contains("\\\\\"")) {
        s = s.replace("\\\\\"", "\\\"")
    }
    // Excel / test_cases.xlsx often stores JSON as a single cell with escaped quotes: {\"messageList\":[...]}
    // Without this, JSONObject cannot parse and message lines never render.
    if (s.contains("\\\"")) {
        s = s.replace("\\\"", "\"")
    }
    // Same sources may also escape structural punctuation (\{ \} \: \, \[ \]).
    s = s.replace(Regex("""\\([{}\[\]:,])"""), "$1")
    if (poslinkJsonDoubleQuotedEnvelope(s)) {
        s = s.substring(1, s.length - 1)
    }
    return s
}

internal fun JSONObject.extractPoslinkMessageLinesFromObject(): List<String> {
    optJSONArray("messageList")?.extractPoslinkMessageLines()?.takeIf { it.isNotEmpty() }?.let { return it }
    optJSONArray("msgList")?.extractPoslinkMessageLines()?.takeIf { it.isNotEmpty() }?.let { return it }
    optStringOrNull("messageList")
        ?.let(::parsePoslinkMessageList)
        ?.takeIf { it.isNotBlank() }
        ?.let { return it.lines().filter { line -> line.isNotBlank() } }
    optStringOrNull("msgList")
        ?.let(::parsePoslinkMessageList)
        ?.takeIf { it.isNotBlank() }
        ?.let { return it.lines().filter { line -> line.isNotBlank() } }
    listOf("MsgInfo", "msgInfo", "message").forEach { key ->
        when (val nested = opt(key)) {
            is JSONArray -> nested.extractPoslinkMessageLines()
                .takeIf { it.isNotEmpty() }
                ?.let { return it }
            is JSONObject -> nested.toPoslinkMessageLine()
                ?.let { return listOf(it) }
        }
    }
    return listOfNotNull(toPoslinkMessageLine())
}

internal fun parsePoslinkShowItemListFromJson(raw: String): List<PoslinkItemDetail> = runCatching {
    val content = raw.trim()
    when {
        content.startsWith("[") -> JSONArray(content).extractPoslinkItemDetails()
        content.startsWith("{") -> {
            val root = JSONObject(content)
            when {
                root.has("messageList") -> root.optJSONArray("messageList")?.extractPoslinkItemDetails().orEmpty()
                root.has("items") -> root.optJSONArray("items")?.extractPoslinkItemDetails().orEmpty()
                root.has("itemList") -> root.optJSONArray("itemList")?.extractPoslinkItemDetails().orEmpty()
                else -> listOfNotNull(root.toPoslinkItemDetailOrNull())
            }
        }
        else -> emptyList()
    }
}.getOrDefault(emptyList())

internal fun parsePoslinkShowItemListLoose(raw: String): List<PoslinkItemDetail> {
    val parsed = mutableListOf<PoslinkItemDetail>()
    val itemDetailBlocks = Regex("""(?i)ItemDetail\s*[:=]\s*\{([^{}]*)\}""")
        .findAll(raw)
        .map { "{${it.groupValues[1]}}" }
        .toList()
    itemDetailBlocks.forEach { block ->
        parseLoosePoslinkItemDetail(block)?.let(parsed::add)
    }
    if (parsed.isNotEmpty()) return parsed

    val objectBlocks = Regex("\\{[^{}]*\\}").findAll(raw).map { it.value }.toList()
    objectBlocks.forEach { block ->
        parseLoosePoslinkItemDetail(block)?.let(parsed::add)
    }
    if (parsed.isNotEmpty()) return parsed

    parseLoosePoslinkItemDetail(raw)?.let(parsed::add)
    return parsed
}

internal fun JSONArray.extractPoslinkItemDetails(): List<PoslinkItemDetail> {
    val items = mutableListOf<PoslinkItemDetail>()
    for (index in 0 until length()) {
        val item = optJSONObject(index) ?: continue
        item.toPoslinkItemDetailOrNull()?.let(items::add)
    }
    return items
}

internal fun JSONObject.toPoslinkItemDetailOrNull(): PoslinkItemDetail? {
    val source = optJSONObject("ItemDetail") ?: this
    val item = PoslinkItemDetail(
        productName = source.optStringOrNull("productName"),
        pluCode = source.optStringOrNull("plUcode"),
        price = source.optStringOrNull("price"),
        unit = source.optStringOrNull("unit"),
        unitPrice = source.optStringOrNull("unitPrice"),
        tax = source.optStringOrNull("tax"),
        quantity = source.optStringOrNull("quantity"),
        productImgUri = source.optStringOrNull("productImgUri"),
        productImgDesc = source.optStringOrNull("productImgDesc")
    )
    return item.takeIf { it.hasMeaningfulContent() }
}

internal fun parseLoosePoslinkItemDetail(raw: String): PoslinkItemDetail? {
    val inner = extractLooseField(raw, "ItemDetail") ?: raw
    val item = PoslinkItemDetail(
        productName = extractLooseField(inner, "productName"),
        pluCode = extractLooseField(inner, "plUcode"),
        price = extractLooseField(inner, "price"),
        unit = extractLooseField(inner, "unit"),
        unitPrice = extractLooseField(inner, "unitPrice"),
        tax = extractLooseField(inner, "tax"),
        quantity = extractLooseField(inner, "quantity"),
        productImgUri = extractLooseField(inner, "productImgUri"),
        productImgDesc = extractLooseField(inner, "productImgDesc")
    )
    return item.takeIf { it.hasMeaningfulContent() }
}

internal fun PoslinkItemDetail.hasMeaningfulContent(): Boolean = listOf(
    productName,
    pluCode,
    price,
    unit,
    unitPrice,
    tax,
    quantity,
    productImgUri,
    productImgDesc
).any { !it.isNullOrBlank() }

internal fun JSONObject.optStringOrNull(key: String): String? {
    val value = opt(key)
    return when {
        value == null || value == JSONObject.NULL -> null
        else -> value.toString().trim().takeIf { it.isNotBlank() }
    }
}

internal fun List<PoslinkItemDetail>.toReadablePoslinkItemText(currencySymbol: String): String =
    mapIndexed { index, item -> item.toReadableLine(index, currencySymbol) }
        .filter { it.isNotBlank() }
        .joinToString("\n\n")

internal fun PoslinkItemDetail.toReadableLine(index: Int, currencySymbol: String): String {
    val symbol = currencySymbol.ifBlank { "$" }
    val name = productName?.takeIf { it.isNotBlank() }
        ?: pluCode?.takeIf { it.isNotBlank() }?.let { "Item $it" }
        ?: "Item ${index + 1}"
    val quantityRaw = quantity?.takeIf { it.isNotBlank() }
    val unitRaw = unit?.takeIf { it.isNotBlank() }
    val unitPriceText = formatPoslinkMoney(unitPrice ?: price, symbol)
    val amountText = formatPoslinkMoney(price, symbol)

    val qtyAndUnit = when {
        quantityRaw.isNullOrBlank() -> ""
        unitRaw.equals("x", ignoreCase = true) -> "x$quantityRaw"
        unitRaw.isNullOrBlank() -> quantityRaw
        else -> "$quantityRaw$unitRaw"
    }
    val atUnitPrice = when {
        unitPriceText.isBlank() -> ""
        unitRaw.equals("x", ignoreCase = true) -> "@$unitPriceText"
        unitRaw.isNullOrBlank() -> "@$unitPriceText"
        else -> "@$unitPriceText/$unitRaw"
    }
    val leadingDetail = listOf(qtyAndUnit, atUnitPrice)
        .filter { it.isNotBlank() }
        .joinToString(" ")
    val detailLine = listOf(leadingDetail, amountText)
        .filter { it.isNotBlank() }
        .joinToString("  ")

    val numberedName = "${index + 1}. $name"
    return if (detailLine.isBlank()) {
        numberedName
    } else {
        "$numberedName\n$detailLine"
    }
}

internal fun formatPoslinkMoney(raw: String?, symbol: String): String {
    val source = raw?.trim().orEmpty()
    if (source.isBlank()) return ""
    val value = source.toBigDecimalOrNull() ?: return "$symbol$source"
    return "$symbol${value.setScale(2, RoundingMode.HALF_UP).toPlainString()}"
}
