package com.paxus.pay.poslinkui.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.paxus.pay.poslinkui.demo.entry.poslink.TextShowingUtils;
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zyz
 * @creat 2026-01-28 14:33
 * @description
 */
public class TextShowingUtilsTest {
    @Before
    public void setUp() {
    }

    /**
     * Test Case 1: Newline characters should be merged into content
     * Input: [Item1] + [LINE] + [Item2]
     * Expected: [Item1\nItem2] (single item)
     */
    @Test
    public void testMergeLineBreaks_BasicNewline() {
        // Arrange
        List<PrintDataItem> input = new ArrayList<>();
        input.add(new PrintDataItem("Line1", new ArrayList<>()));
        input.add(new PrintDataItem("", Arrays.asList(PrintDataItem.LINE)));
        input.add(new PrintDataItem("Line2", new ArrayList<>()));

        // Act
        List<PrintDataItem> result = invokePrivateMethod(input);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Line1\nLine2", result.get(0).getContent());
    }

    /**
     * Test Case 2: Font size commands should trigger splitting
     * Input: [\1 Small] + [LINE] + [\2 Normal]
     * Expected: 2 separate items
     */
    @Test
    public void testMergeLineBreaks_FontSizeSplitting() {
        // Arrange
        List<PrintDataItem> input = new ArrayList<>();
        input.add(new PrintDataItem("Small", Arrays.asList(PrintDataItem.SMALL_FONT)));
        input.add(new PrintDataItem("", Arrays.asList(PrintDataItem.LINE)));
        input.add(new PrintDataItem("Normal", Arrays.asList(PrintDataItem.NORMAL_FONT)));

        // Act
        List<PrintDataItem> result = invokePrivateMethod(input);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getCmds().contains(PrintDataItem.SMALL_FONT));
        assertTrue(result.get(1).getCmds().contains(PrintDataItem.NORMAL_FONT));
    }

    /**
     * Test Case 3: Alignment commands should trigger splitting
     * Input: [\L Left] + [\R Right]
     * Expected: 2 separate items
     */
    @Test
    public void testMergeLineBreaks_AlignmentSplitting() {
        // Arrange
        List<PrintDataItem> input = new ArrayList<>();
        input.add(new PrintDataItem("Left", Arrays.asList(PrintDataItem.LEFT_ALIGN)));
        input.add(new PrintDataItem("Right", Arrays.asList(PrintDataItem.RIGHT_ALIGN)));

        // Act
        List<PrintDataItem> result = invokePrivateMethod(input);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getCmds().contains(PrintDataItem.LEFT_ALIGN));
        assertTrue(result.get(1).getCmds().contains(PrintDataItem.RIGHT_ALIGN));
    }

    /**
     * Test Case 4: Multiple newlines should be preserved
     * Input: [A] + [LINE] + [LINE] + [B]
     * Expected: [A\n\nB] (single item with double newline)
     */
    @Test
    public void testMergeLineBreaks_MultipleNewlines() {
        // Arrange
        List<PrintDataItem> input = new ArrayList<>();
        input.add(new PrintDataItem("A", new ArrayList<>()));
        input.add(new PrintDataItem("", Arrays.asList(PrintDataItem.LINE)));
        input.add(new PrintDataItem("", Arrays.asList(PrintDataItem.LINE)));
        input.add(new PrintDataItem("B", new ArrayList<>()));

        // Act
        List<PrintDataItem> result = invokePrivateMethod(input);

        // Assert
        assertEquals(1, result.size());
        assertEquals("A\n\nB", result.get(0).getContent());
    }

    /**
     * Test Case 5: Null input should return safely
     */
    @Test
    public void testMergeLineBreaks_NullInput() {
        // Act
        List<PrintDataItem> result = invokePrivateMethod(null);

        // Assert
        assertNull(result);
    }

    /**
     * Test Case 6: Empty list should return empty list
     */
    @Test
    public void testMergeLineBreaks_EmptyInput() {
        // Arrange
        List<PrintDataItem> input = new ArrayList<>();

        // Act
        List<PrintDataItem> result = invokePrivateMethod(input);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test Case 7: Complex scenario - mixed formatting and newlines
     * Input: [\1 Small] + [LINE] + [Text] + [\3 Big]
     * Expected: [\1 Small\nText] + [\3 Big]
     */
    @Test
    public void testMergeLineBreaks_ComplexScenario() {
        // Arrange
        List<PrintDataItem> input = new ArrayList<>();
        input.add(new PrintDataItem("Small", Arrays.asList(PrintDataItem.SMALL_FONT)));
        input.add(new PrintDataItem("", Arrays.asList(PrintDataItem.LINE)));
        input.add(new PrintDataItem("Text", new ArrayList<>()));
        input.add(new PrintDataItem("Big", Arrays.asList(PrintDataItem.BIG_FONT)));

        // Act
        List<PrintDataItem> result = invokePrivateMethod(input);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Small\nText", result.get(0).getContent());
        assertEquals("Big", result.get(1).getContent());
    }

    private List<PrintDataItem> invokePrivateMethod(List<PrintDataItem> input) {
        try {
            java.lang.reflect.Method method = TextShowingUtils.class.getDeclaredMethod(
                    "mergeLineBreaksContent", List.class);
            method.setAccessible(true);
            return (List<PrintDataItem>) method.invoke(null, input);
        } catch (Exception e) {
            fail("Failed to invoke private method: " + e.getMessage());
            return null;
        }
    }
}
