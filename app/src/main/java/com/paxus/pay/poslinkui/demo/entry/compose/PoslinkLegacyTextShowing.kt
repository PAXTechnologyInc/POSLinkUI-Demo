package com.paxus.pay.poslinkui.demo.entry.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataConverter
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem

private val POSLINK_FONT_SMALL = 20.sp
private val POSLINK_FONT_NORMAL = 24.sp
private val POSLINK_FONT_BIG = 28.sp
private val POSLINK_INLINE_LINE_HEIGHT = 36.4.sp
private val POSLINK_FONT_PADDING_NORMAL = 4.dp
private val POSLINK_FONT_PADDING_SMALL = 8.dp

internal data class PoslinkLegacyTextCell(
    val text: String,
    val align: TextAlign,
    val bold: Boolean,
    val fontSize: TextUnit,
    val topPadding: Dp
)

internal data class PoslinkLegacySimpleTextLine(
    val text: String,
    val align: TextAlign,
    val bold: Boolean,
    val fontSize: TextUnit
)

internal data class PoslinkShowDialogTitleSlot(
    val text: AnnotatedString,
    val align: TextAlign
)

internal fun normalizePoslinkControlCommands(raw: String): String {
    if (raw.isBlank()) return raw
    return raw.replace(Regex("""\\+([LRCB123n])"""), """\\$1""")
}

@Composable
internal fun PoslinkLegacyTitleLikeText(
    raw: String,
    modifier: Modifier = Modifier,
    supportLineSep: Boolean = false,
    allowWrap: Boolean = supportLineSep,
    maxLines: Int = if (allowWrap) Int.MAX_VALUE else 1,
    allowCharacterWrap: Boolean = false,
    textColor: Color = PosLinkDesignTokens.PrimaryTextColor
) {
    val cells = remember(raw, supportLineSep) {
        buildPoslinkTitleLikeCells(raw = raw, supportLineSep = supportLineSep)
    }
    PoslinkLegacyInlineTextContent(
        cells = cells,
        modifier = modifier,
        allowWrap = allowWrap,
        maxLines = maxLines,
        allowCharacterWrap = allowCharacterWrap,
        textColor = textColor
    )
}

@Composable
internal fun PoslinkLegacyValueLikeText(
    raw: String,
    modifier: Modifier = Modifier,
    supportLineSep: Boolean = false,
    allowWrap: Boolean = supportLineSep,
    maxLines: Int = if (allowWrap) Int.MAX_VALUE else 1,
    allowCharacterWrap: Boolean = false,
    textColor: Color = PosLinkDesignTokens.PrimaryTextColor
) {
    val cells = remember(raw, supportLineSep) {
        buildPoslinkValueLikeCells(raw = raw, supportLineSep = supportLineSep)
    }
    PoslinkLegacyInlineTextContent(
        cells = cells,
        modifier = modifier,
        allowWrap = allowWrap,
        maxLines = maxLines,
        allowCharacterWrap = allowCharacterWrap,
        textColor = textColor
    )
}

@Composable
internal fun PoslinkShowDialogTitleLikeText(
    raw: String,
    modifier: Modifier = Modifier,
    supportLineSep: Boolean = false,
    textColor: Color = PosLinkDesignTokens.PrimaryTextColor
) {
    val slots = remember(raw, supportLineSep) {
        buildPoslinkShowDialogTitleSlots(raw = raw, supportLineSep = supportLineSep)
    }
    if (slots.isEmpty()) return
    Row(modifier = modifier.fillMaxWidth()) {
        slots.forEach { slot ->
            Text(
                text = slot.text,
                modifier = Modifier.weight(1f),
                color = textColor,
                textAlign = slot.align,
                softWrap = supportLineSep,
                maxLines = if (supportLineSep) Int.MAX_VALUE else 1,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = POSLINK_FONT_NORMAL,
                    lineHeight = POSLINK_INLINE_LINE_HEIGHT
                )
            )
        }
    }
}

@Composable
internal fun PoslinkLegacySimpleTextList(
    raw: String,
    modifier: Modifier = Modifier,
    textColor: Color = PosLinkDesignTokens.PrimaryTextColor
) {
    val lines = remember(raw) { buildPoslinkSimpleTextLines(raw) }
    if (lines.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth()) {
        lines.forEach { line ->
            Text(
                text = line.text,
                modifier = Modifier.fillMaxWidth(),
                color = textColor,
                textAlign = line.align,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (line.bold) FontWeight.Bold else FontWeight.Normal,
                    fontSize = line.fontSize,
                    lineHeight = POSLINK_INLINE_LINE_HEIGHT
                )
            )
        }
    }
}

@Composable
internal fun PoslinkLegacyInlineTextContent(
    cells: List<PoslinkLegacyTextCell>,
    modifier: Modifier = Modifier,
    allowWrap: Boolean = false,
    maxLines: Int = if (allowWrap) Int.MAX_VALUE else 1,
    allowCharacterWrap: Boolean = false,
    textColor: Color = PosLinkDesignTokens.PrimaryTextColor
) {
    if (cells.isEmpty()) return
    Row(modifier = modifier.fillMaxWidth()) {
        cells.forEach { cell ->
            Text(
                text = if (allowWrap && allowCharacterWrap) {
                    cell.text.enableLegacyCharacterWrap()
                } else {
                    cell.text
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(top = cell.topPadding),
                color = textColor,
                textAlign = cell.align,
                maxLines = maxLines,
                softWrap = allowWrap,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (cell.bold) FontWeight.Bold else FontWeight.Normal,
                    fontSize = cell.fontSize,
                    lineHeight = POSLINK_INLINE_LINE_HEIGHT
                )
            )
        }
    }
}

private fun String.enableLegacyCharacterWrap(): String {
    if (isBlank()) return this
    val breaker = '\u200B'
    val out = StringBuilder(length * 2)
    forEachIndexed { index, ch ->
        out.append(ch)
        if (ch != '\n' && index != lastIndex) {
            out.append(breaker)
        }
    }
    return out.toString()
}

internal fun buildPoslinkTitleLikeCells(raw: String, supportLineSep: Boolean = false): List<PoslinkLegacyTextCell> =
    buildPoslinkInlineCells(
        raw = raw,
        defaultAlign = PrintDataItem.CENTER_ALIGN,
        supportLineSep = supportLineSep,
        mergeLineSepAsSingleText = false
    )

internal fun buildPoslinkValueLikeCells(raw: String, supportLineSep: Boolean = false): List<PoslinkLegacyTextCell> =
    buildPoslinkInlineCells(
        raw = raw,
        defaultAlign = PrintDataItem.LEFT_ALIGN,
        supportLineSep = supportLineSep,
        mergeLineSepAsSingleText = supportLineSep
    )

internal fun buildPoslinkSimpleTextLines(raw: String): List<PoslinkLegacySimpleTextLine> {
    if (raw.isBlank()) return emptyList()
    return normalizePoslinkControlCommands(raw)
        .replace(PrintDataItem.LINE_SEP, PrintDataItem.LINE)
        .split(PrintDataItem.LINE)
        .map { line -> parsePoslinkSimpleTextLine(line) }
}

private enum class PoslinkShowDialogSlotKey {
    LEFT,
    CENTER,
    RIGHT
}

private class PoslinkShowDialogTitleSlotBuilder(
    val align: TextAlign
) {
    private val text = StringBuilder()
    private val spans = mutableListOf<Pair<IntRange, SpanStyle>>()

    fun append(segment: String, bold: Boolean, fontSize: TextUnit) {
        if (segment.isEmpty()) return
        val start = text.length
        text.append(segment)
        val endExclusive = text.length
        spans += (start until endExclusive) to SpanStyle(
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = fontSize
        )
    }

    fun build(): PoslinkShowDialogTitleSlot {
        val annotated = AnnotatedString.Builder(text.toString())
        spans.forEach { (range, style) ->
            annotated.addStyle(style, range.first, range.last + 1)
        }
        return PoslinkShowDialogTitleSlot(
            text = annotated.toAnnotatedString(),
            align = align
        )
    }

    fun isBlank(): Boolean = text.isEmpty()
}

private data class PoslinkShowDialogParseState(
    val slot: PoslinkShowDialogSlotKey = PoslinkShowDialogSlotKey.CENTER,
    val bold: Boolean = false,
    val fontSize: TextUnit = POSLINK_FONT_NORMAL
)

private data class PoslinkShowDialogCommandResult(
    val nextIndex: Int,
    val state: PoslinkShowDialogParseState,
    val textToAppend: String? = null
)

private fun poslinkShowDialogAlignFor(slot: PoslinkShowDialogSlotKey): TextAlign = when (slot) {
    PoslinkShowDialogSlotKey.LEFT -> TextAlign.Left
    PoslinkShowDialogSlotKey.CENTER -> TextAlign.Center
    PoslinkShowDialogSlotKey.RIGHT -> TextAlign.Right
}

private fun poslinkShowDialogSlotFor(cmd: Char): PoslinkShowDialogSlotKey = when (cmd) {
    'L' -> PoslinkShowDialogSlotKey.LEFT
    'R' -> PoslinkShowDialogSlotKey.RIGHT
    else -> PoslinkShowDialogSlotKey.CENTER
}

private fun poslinkShowDialogFontFor(cmd: Char): TextUnit = when (cmd) {
    '1' -> POSLINK_FONT_SMALL
    '3' -> POSLINK_FONT_BIG
    else -> POSLINK_FONT_NORMAL
}

private fun consumePoslinkShowDialogCommand(
    normalized: String,
    index: Int,
    state: PoslinkShowDialogParseState,
    supportLineSep: Boolean
): PoslinkShowDialogCommandResult? {
    if (normalized[index] != '\\' || index + 1 >= normalized.length) return null
    val cmd = normalized[index + 1]
    return when (cmd) {
        'L', 'C', 'R' -> PoslinkShowDialogCommandResult(
            nextIndex = index + 2,
            state = PoslinkShowDialogParseState(slot = poslinkShowDialogSlotFor(cmd))
        )

        'B' -> PoslinkShowDialogCommandResult(
            nextIndex = index + 2,
            state = state.copy(bold = true)
        )

        '1', '2', '3' -> PoslinkShowDialogCommandResult(
            nextIndex = index + 2,
            state = state.copy(fontSize = poslinkShowDialogFontFor(cmd))
        )

        'n' -> {
            if (!supportLineSep) return null
            PoslinkShowDialogCommandResult(
                nextIndex = index + 2,
                state = state,
                textToAppend = "\n"
            )
        }

        else -> null
    }
}

private fun ensurePoslinkShowDialogCompanionSlots(
    slots: MutableMap<PoslinkShowDialogSlotKey, PoslinkShowDialogTitleSlot>
) {
    if (PoslinkShowDialogSlotKey.CENTER !in slots) return
    if (PoslinkShowDialogSlotKey.LEFT !in slots) {
        slots[PoslinkShowDialogSlotKey.LEFT] = PoslinkShowDialogTitleSlot(
            text = AnnotatedString(" "),
            align = TextAlign.Left
        )
    }
    if (PoslinkShowDialogSlotKey.RIGHT !in slots) {
        slots[PoslinkShowDialogSlotKey.RIGHT] = PoslinkShowDialogTitleSlot(
            text = AnnotatedString(" "),
            align = TextAlign.Right
        )
    }
}

internal fun buildPoslinkShowDialogTitleSlots(
    raw: String,
    supportLineSep: Boolean = false
): List<PoslinkShowDialogTitleSlot> {
    if (raw.isBlank()) return emptyList()
    val normalized = normalizePoslinkControlCommands(raw).replace("\r", "")
    val slotBuilders = linkedMapOf<PoslinkShowDialogSlotKey, PoslinkShowDialogTitleSlotBuilder>()
    var state = PoslinkShowDialogParseState()

    fun builderFor(slot: PoslinkShowDialogSlotKey): PoslinkShowDialogTitleSlotBuilder {
        return slotBuilders.getOrPut(slot) {
            PoslinkShowDialogTitleSlotBuilder(
                align = poslinkShowDialogAlignFor(slot)
            )
        }
    }

    var index = 0
    while (index < normalized.length) {
        val command = consumePoslinkShowDialogCommand(normalized, index, state, supportLineSep)
        if (command != null) {
            state = command.state
            val builder = builderFor(state.slot)
            command.textToAppend?.let { text ->
                builder.append(text, state.bold, state.fontSize)
            }
            index = command.nextIndex
            continue
        }
        builderFor(state.slot).append(normalized[index].toString(), state.bold, state.fontSize)
        index++
    }

    val slots = slotBuilders
        .filterValues { !it.isBlank() }
        .mapValues { (_, builder) -> builder.build() }
        .toMutableMap()

    ensurePoslinkShowDialogCompanionSlots(slots)

    return listOfNotNull(
        slots[PoslinkShowDialogSlotKey.LEFT],
        slots[PoslinkShowDialogSlotKey.CENTER],
        slots[PoslinkShowDialogSlotKey.RIGHT]
    )
}

private fun buildPoslinkInlineCells(
    raw: String,
    defaultAlign: String,
    supportLineSep: Boolean,
    mergeLineSepAsSingleText: Boolean
): List<PoslinkLegacyTextCell> {
    if (raw.isBlank()) return emptyList()
    val parsedItems = parsePoslinkPrintDataItems(raw = raw, supportLineSep = supportLineSep)
    if (parsedItems.isEmpty()) return emptyList()
    if (mergeLineSepAsSingleText && containsLineBreak(parsedItems)) {
        val mergedText = buildString {
            parsedItems.forEach { item ->
                when {
                    item.cmds.contains(PrintDataItem.LINE) || item.cmds.contains(PrintDataItem.LINE_SEP) -> append('\n')
                    !item.content.isNullOrEmpty() -> append(item.content)
                }
            }
        }
        return listOf(
            PrintDataItem(
                mergedText,
                mutableListOf(defaultAlign)
            ).toLegacyTextCell()
        )
    }
    return filterPoslinkLegacyItems(parsedItems, defaultAlign)
        .filterNot { it.cmds.contains(PrintDataItem.LINE) || it.cmds.contains(PrintDataItem.LINE_SEP) }
        .map(PrintDataItem::toLegacyTextCell)
}

private fun parsePoslinkPrintDataItems(raw: String, supportLineSep: Boolean): List<PrintDataItem> {
    val normalized = normalizePoslinkControlCommands(raw).replace("\r", "")
    return PrintDataConverter()
        .parse(normalized, supportLineSep)
        .printDataItems
        .filterNotNull()
}

private fun parsePoslinkSimpleTextLine(raw: String): PoslinkLegacySimpleTextLine {
    var text = raw
    var align = TextAlign.Left
    var bold = false
    var fontSize = POSLINK_FONT_NORMAL

    while (text.length >= 2 && text[0] == '\\') {
        when (text[1]) {
            'L' -> {
                align = TextAlign.Left
                text = text.drop(2)
            }

            'R' -> {
                align = TextAlign.Right
                text = text.drop(2)
            }

            'C' -> {
                align = TextAlign.Center
                text = text.drop(2)
            }

            'B' -> {
                bold = true
                text = text.drop(2)
            }

            '1' -> {
                fontSize = POSLINK_FONT_SMALL
                text = text.drop(2)
            }

            '2' -> {
                fontSize = POSLINK_FONT_NORMAL
                text = text.drop(2)
            }

            '3' -> {
                fontSize = POSLINK_FONT_BIG
                text = text.drop(2)
            }

            else -> break
        }
    }

    return PoslinkLegacySimpleTextLine(
        text = text,
        align = align,
        bold = bold,
        fontSize = fontSize
    )
}

private fun PrintDataItem.toLegacyTextCell(): PoslinkLegacyTextCell {
    val fontSize = when {
        cmds.contains(PrintDataItem.BIG_FONT) -> POSLINK_FONT_BIG
        cmds.contains(PrintDataItem.SMALL_FONT) -> POSLINK_FONT_SMALL
        else -> POSLINK_FONT_NORMAL
    }
    val topPadding = when (fontSize) {
        POSLINK_FONT_SMALL -> POSLINK_FONT_PADDING_SMALL
        POSLINK_FONT_NORMAL -> POSLINK_FONT_PADDING_NORMAL
        else -> 0.dp
    }
    val align = when {
        cmds.contains(PrintDataItem.RIGHT_ALIGN) -> TextAlign.Right
        cmds.contains(PrintDataItem.CENTER_ALIGN) -> TextAlign.Center
        else -> TextAlign.Left
    }
    return PoslinkLegacyTextCell(
        text = content.orEmpty(),
        align = align,
        bold = cmds.contains(PrintDataItem.BOLD),
        fontSize = fontSize,
        topPadding = topPadding
    )
}

private fun containsAlign(item: PrintDataItem): Boolean =
    item.cmds.contains(PrintDataItem.LEFT_ALIGN) ||
        item.cmds.contains(PrintDataItem.RIGHT_ALIGN) ||
        item.cmds.contains(PrintDataItem.CENTER_ALIGN)

private fun containsLineBreak(items: List<PrintDataItem>): Boolean =
    items.any { item ->
        item.cmds.contains(PrintDataItem.LINE) || item.cmds.contains(PrintDataItem.LINE_SEP)
    }

internal fun filterPoslinkLegacyItems(
    printDataItems: List<PrintDataItem>,
    defaultAlign: String
): List<PrintDataItem> {
    val tempList = mutableListOf<PrintDataItem>()
    if (printDataItems.isEmpty()) return tempList

    val firstItem = printDataItems.first()
    if (!containsAlign(firstItem)) {
        firstItem.cmds.add(defaultAlign)
    }

    var leftItem: PrintDataItem? = null
    var rightItem: PrintDataItem? = null
    var centerItem: PrintDataItem? = null
    var boldItem: PrintDataItem? = null

    printDataItems.forEach { item ->
        when {
            item.cmds.contains(PrintDataItem.LEFT_ALIGN) -> leftItem = item
            item.cmds.contains(PrintDataItem.RIGHT_ALIGN) -> rightItem = item
            item.cmds.contains(PrintDataItem.CENTER_ALIGN) -> centerItem = item
            item.cmds.contains(PrintDataItem.BOLD) -> boldItem = item
            else -> tempList.add(item)
        }
    }

    leftItem?.let(tempList::add)
    boldItem?.let(tempList::add)
    rightItem?.let(tempList::add)
    centerItem?.let(tempList::add)

    return sortPoslinkLegacyItems(tempList)
}

internal fun sortPoslinkLegacyItems(items: List<PrintDataItem>): List<PrintDataItem> {
    val ordering = collectPoslinkLegacyItemOrdering(items)
    ordering.leftItem?.let { ordering.tempList.add(0, it) }
    ordering.rightItem?.let(ordering.tempList::add)
    insertCenteredPoslinkLegacyItem(
        tempList = ordering.tempList,
        centerItem = ordering.centerItem,
        leftCount = ordering.leftCount,
        rightCount = ordering.rightCount
    )
    padCenteredPoslinkLegacyPair(ordering.tempList)
    return ordering.tempList
}

private data class PoslinkLegacyItemOrdering(
    val tempList: MutableList<PrintDataItem>,
    val leftItem: PrintDataItem?,
    val rightItem: PrintDataItem?,
    val centerItem: PrintDataItem?,
    val leftCount: Int,
    val rightCount: Int
)

private fun collectPoslinkLegacyItemOrdering(items: List<PrintDataItem>): PoslinkLegacyItemOrdering {
    var leftCount = 0
    var rightCount = 0
    var centerCount = 0
    val tempList = mutableListOf<PrintDataItem>()
    var leftItem: PrintDataItem? = null
    var rightItem: PrintDataItem? = null
    var centerItem: PrintDataItem? = null

    items.forEach { item ->
        when {
            item.cmds.contains(PrintDataItem.LEFT_ALIGN) && leftCount == 0 -> {
                leftCount++
                leftItem = item
            }

            item.cmds.contains(PrintDataItem.RIGHT_ALIGN) && rightCount == 0 -> {
                rightCount++
                rightItem = item
            }

            item.cmds.contains(PrintDataItem.CENTER_ALIGN) && centerCount == 0 -> {
                centerCount++
                centerItem = item
            }

            else -> tempList.add(item)
        }
    }

    return PoslinkLegacyItemOrdering(
        tempList = tempList,
        leftItem = leftItem,
        rightItem = rightItem,
        centerItem = centerItem,
        leftCount = leftCount,
        rightCount = rightCount
    )
}

private fun insertCenteredPoslinkLegacyItem(
    tempList: MutableList<PrintDataItem>,
    centerItem: PrintDataItem?,
    leftCount: Int,
    rightCount: Int
) {
    centerItem ?: return
    when {
        tempList.size > 2 -> tempList.add(tempList.size / 2 + 1, centerItem)
        tempList.size == 2 -> tempList.add(tempList.size / 2, centerItem)
        tempList.size == 1 && leftCount != 0 -> tempList.add(1, centerItem)
        tempList.size == 1 && rightCount != 0 -> tempList.add(0, centerItem)
        else -> tempList.add(centerItem)
    }
}

private fun padCenteredPoslinkLegacyPair(tempList: MutableList<PrintDataItem>) {
    if (tempList.size != 2) return
    var containCenter = false
    var addLeft = false
    var addRight = false
    var addBold = false

    tempList.forEach { item ->
        when {
            item.cmds.contains(PrintDataItem.CENTER_ALIGN) -> containCenter = true
            item.cmds.contains(PrintDataItem.LEFT_ALIGN) -> addRight = true
            item.cmds.contains(PrintDataItem.RIGHT_ALIGN) -> addLeft = true
            item.cmds.contains(PrintDataItem.BOLD) -> addBold = true
        }
    }

    if (!containCenter) return
    if (addLeft) {
        tempList.add(0, PrintDataItem(" ", mutableListOf(PrintDataItem.LEFT_ALIGN)))
    }
    if (addRight || addBold) {
        tempList.add(PrintDataItem(" ", mutableListOf(PrintDataItem.RIGHT_ALIGN)))
    }
}
