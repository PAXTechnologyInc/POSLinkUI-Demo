package com.paxus.pay.poslinkui.demo.entry.compose

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PoslinkLegacyTextShowingTest {

    @Test
    fun normalizePoslinkControlCommands_collapsesEscapedCommands() {
        assertEquals("\\LLeft\\RRight\\BBold", normalizePoslinkControlCommands("\\\\LLeft\\\\RRight\\\\BBold"))
    }

    @Test
    fun buildPoslinkValueLikeCells_appliesLegacyOrderingAndStyles() {
        val cells = buildPoslinkValueLikeCells("\\LLeft\\RRight\\BBold Word")

        assertEquals(3, cells.size)
        assertEquals("Left", cells[0].text)
        assertEquals(TextAlign.Left, cells[0].align)
        assertEquals("Bold Word", cells[1].text)
        assertTrue(cells[1].bold)
        assertEquals("Right", cells[2].text)
        assertEquals(TextAlign.Right, cells[2].align)
    }

    @Test
    fun buildPoslinkTitleLikeCells_keepsCenterItemInMiddleSlot() {
        val cells = buildPoslinkTitleLikeCells("\\LLeft\\CCenter")

        assertEquals(3, cells.size)
        assertEquals("Left", cells[0].text)
        assertEquals("Center", cells[1].text)
        assertEquals(TextAlign.Center, cells[1].align)
        assertEquals(" ", cells[2].text)
    }

    @Test
    fun buildPoslinkValueLikeCells_mergesLineSeparatorsForLegacyViewListBehavior() {
        val cells = buildPoslinkValueLikeCells("\\LLine1\\nLine2", supportLineSep = true)

        assertEquals(1, cells.size)
        assertEquals("Line1\nLine2", cells[0].text)
        assertEquals(TextAlign.Left, cells[0].align)
    }

    @Test
    fun buildPoslinkSimpleTextLines_parsesLeadingCommandsPerLine() {
        val lines = buildPoslinkSimpleTextLines("\\1Small\\n\\BBold")

        assertEquals(2, lines.size)
        assertEquals("Small", lines[0].text)
        assertEquals("Bold", lines[1].text)
        assertTrue(lines[1].bold)
    }

    @Test
    fun buildPoslinkShowDialogTitleSlots_keepsBoldTextInRightAlignedSlot() {
        val slots = buildPoslinkShowDialogTitleSlots("\\LLeft\\RRight\\BBold Word", supportLineSep = true)

        assertEquals(2, slots.size)
        assertEquals("Left", slots[0].text.text)
        assertEquals(TextAlign.Left, slots[0].align)
        assertEquals("RightBold Word", slots[1].text.text)
        assertEquals(TextAlign.Right, slots[1].align)
        assertTrue(slots[1].text.spanStyles.any { it.item.fontWeight == FontWeight.Bold })
    }
}
