package com.paxus.pay.poslinkui.demo.entry.poslink;

import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.BIG_FONT;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.BOLD;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.CENTER_ALIGN;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.LEFT_ALIGN;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.LINE;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.LINE_SEP;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.NORMAL_FONT;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.RIGHT_ALIGN;
import static com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem.SMALL_FONT;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.pax.us.pay.ui.constant.entry.PoslinkEntry;
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataConverter;
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem;
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataItemContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Utils for POSLink Entry
 * {@value PoslinkEntry#ACTION_SHOW_ITEM}
 */
public class TextShowingUtils {
    public static final float FONT_SMALL_SP = 20;
    public static final float FONT_NORMAL_SP = 24;
    public static final float FONT_BIG_SP = 28;

    public static List<TextView> getTextViewList(Context context, String line) {
        line = line.replaceAll(LINE_SEP, LINE);

        List<TextView> list = new ArrayList<>();

        String[] lines = line.split(LINE);
        for(String l: lines){
            list.add(getTextView(context,l));
        }
        return list;
    }

    public static TextView getTextView(Context context, String line) {
        line = line.replaceAll(LINE_SEP, LINE);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_NORMAL_SP);
        StringBuilder text = new StringBuilder(line);
        while (text.length() > 0) {
            if (text.indexOf(LEFT_ALIGN) == 0) {
                text.delete(0, LEFT_ALIGN.length());

                layoutParams.gravity = Gravity.START;
                textView.setGravity(Gravity.START);
            } else if (text.indexOf(RIGHT_ALIGN) == 0) {
                text.delete(0, RIGHT_ALIGN.length());

                layoutParams.gravity = Gravity.END;
                textView.setGravity(Gravity.END);
            } else if (text.indexOf(CENTER_ALIGN) == 0) {
                text.delete(0, CENTER_ALIGN.length());

                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
            } else if (text.indexOf(BOLD) == 0) {
                text.delete(0, BOLD.length());

                TextPaint textPaint = textView.getPaint();
                textPaint.setFakeBoldText(true);
            } else if (text.indexOf(SMALL_FONT) == 0) {
                text.delete(0, SMALL_FONT.length());

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_SMALL_SP);
            } else if (text.indexOf(BIG_FONT) == 0) {
                text.delete(0, BIG_FONT.length());

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_BIG_SP);
            } else if (text.indexOf(NORMAL_FONT) == 0) {
                text.delete(0, NORMAL_FONT.length());

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, FONT_NORMAL_SP);
            } else {
                break;
            }
        }

        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        return textView;
    }

    // overload func,add supportLineSep param
    public static List<TextView> getTitleViewList(Context context, String data, LinearLayout.LayoutParams layoutParams, @ColorInt int color, float textSize) {
        return getTitleViewList(context, data, layoutParams, color, textSize, false);
    }

    // impl
    public static List<TextView> getTitleViewList(Context context, String data, LinearLayout.LayoutParams layoutParams, @ColorInt int color, float textSize, boolean supportLineSep) {
        if (TextUtils.isEmpty(data))
            data = "";

        List<TextView> textViewList = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<String, List<String>>() {
            {
                put(PrintDataConverter.TEXT_SHOWING_LIST, PrintDataItem.TEXT_SHOWING_LIST);
            }
        };

        try {
            // Parse according to supportLineSep parameter
            PrintDataItemContainer printDataItemContainer = PrintDataConverter.parse(data, map, supportLineSep);

            List<PrintDataItem> parsedItems = printDataItemContainer.getPrintDataItems();

            // If line break is supported, merge consecutive non-alignment items (preserve line breaks)
            if (supportLineSep) {
                parsedItems = mergeItemsWithLineBreak(parsedItems);
            }

            List<PrintDataItem> printDataItemList = filterItems(parsedItems, CENTER_ALIGN);

            // If there's only one item
            if (printDataItemList.size() == 1) {
                PrintDataItem printDataItem = printDataItemList.get(0);
                if (printDataItem.getCmds().size() == 0 || (printDataItem.getCmds().size() == 1
                        && printDataItem.getCmds().get(0).equals(BOLD))) {
                    printDataItem.getCmds().add(CENTER_ALIGN);
                }

                TextView textView = getTitleTextView(context, printDataItem, layoutParams, color, textSize);
                customizeFontSize(textView, printDataItem);
                textViewList.add(textView);
            } else {
                // Multiple items
                for (int i = 0; i < printDataItemList.size(); i++) {
                    PrintDataItem printDataItem = printDataItemList.get(i);
                    TextView textView = getTitleTextView(context, printDataItem, layoutParams, color, textSize);
                    customizeFontSize(textView, printDataItem);

                    if (textView != null) {
                        // If line break is supported, don't set single line; otherwise set single line
                        if (!supportLineSep) {
                            textView.setSingleLine();
                        }
                        textViewList.add(textView);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return textViewList;
    }

    /**
     * Merge items containing line breaks, combining content before and after line breaks into the same item
     * But preserve alignment commands as split points
     */
    private static List<PrintDataItem> mergeItemsWithLineBreak(List<PrintDataItem> items) {
        List<PrintDataItem> result = new ArrayList<>();
        StringBuilder contentBuilder = new StringBuilder();
        List<String> currentCmds = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            PrintDataItem item = items.get(i);

            // If contains alignment command, need to create new TextView
            if (containsAlign(item)) {
                // Save previously accumulated content first
                if (contentBuilder.length() > 0 || !currentCmds.isEmpty()) {
                    result.add(new PrintDataItem(contentBuilder.toString(), new ArrayList<>(currentCmds)));
                    contentBuilder.setLength(0);
                    currentCmds.clear();
                }

                // Collect current item's commands (except LINE)
                for (String cmd : item.getCmds()) {
                    if (!cmd.equals(LINE) && !currentCmds.contains(cmd)) {
                        currentCmds.add(cmd);
                    }
                }

                // Add content
                if (!TextUtils.isEmpty(item.getContent())) {
                    contentBuilder.append(item.getContent());
                }
            } else if (item.getCmds().contains(LINE)) {
                // Line break: add to current content
                contentBuilder.append("\n");
            } else {
                // Normal content or font command: add to current content
                if (!TextUtils.isEmpty(item.getContent())) {
                    contentBuilder.append(item.getContent());
                }
                // Collect non-alignment commands
                for (String cmd : item.getCmds()) {
                    if (!cmd.equals(LINE) && !currentCmds.contains(cmd)) {
                        currentCmds.add(cmd);
                    }
                }
            }
        }

        // Add the last item
        if (contentBuilder.length() > 0 || !currentCmds.isEmpty()) {
            result.add(new PrintDataItem(contentBuilder.toString(), currentCmds));
        }

        return result;
    }

    public static List<PrintDataItem> filterItems(List<PrintDataItem> printDataItems, String defaultAlign) {
        List<PrintDataItem> tempList = new ArrayList<>();

        if (printDataItems != null && printDataItems.size() > 0) {
            PrintDataItem firstItem = printDataItems.get(0);
            if (!containsAlign(firstItem)) {
                firstItem.getCmds().add(defaultAlign);
            }
            PrintDataItem leftItem = null, rightItem = null, centerItem = null, boldItem = null;
            Iterator<PrintDataItem> iterator = printDataItems.iterator();

            //Confirm left, center, right item.
            while (iterator.hasNext()) {
                PrintDataItem printDataItem = iterator.next();
                if (printDataItem.getCmds().contains(LEFT_ALIGN)) {
                    leftItem = printDataItem;
                } else if (printDataItem.getCmds().contains(RIGHT_ALIGN)) {
                    rightItem = printDataItem;
                } else if (printDataItem.getCmds().contains(CENTER_ALIGN)) {
                    centerItem = printDataItem;
                } else if (printDataItem.getCmds().contains(BOLD)) {
                    boldItem = printDataItem;
                } else {
                    tempList.add(printDataItem);
                }
            }

            if (leftItem != null) {
                tempList.add(leftItem);
            }

            if (boldItem != null) {
                tempList.add(boldItem);
            }
            if (rightItem != null) {
                tempList.add(rightItem);
            }

            if (centerItem != null) {
                tempList.add(centerItem);
            }
        }
        //After confirming left, center, right item, sorted.
        return sortList(tempList);
    }

    private static boolean containsAlign(PrintDataItem printDataItem) {
        return printDataItem.getCmds().contains(LEFT_ALIGN)
                || printDataItem.getCmds().contains(RIGHT_ALIGN)
                || printDataItem.getCmds().contains(CENTER_ALIGN);
    }

    private static boolean containsLineBreak(List<PrintDataItem> items) {
        for (PrintDataItem item : items) {
            if (item.getCmds().contains(LINE)) {
                return true;
            }
        }
        return false;
    }
    public static TextView getTitleTextView(Context context, PrintDataItem printDataItem, LinearLayout.LayoutParams layoutParams, @ColorInt int color, float textSize) {
        return generateTextView(context, printDataItem, layoutParams, color, textSize);
    }

    public static List<PrintDataItem> sortList(List<PrintDataItem> list) {
        int leftCount = 0;
        int rightCount = 0;
        int centerCount = 0;

        List<PrintDataItem> tempList = new ArrayList<>();
        PrintDataItem leftItem = null, rightItem = null, centerItem = null;

        for (int i = 0; i < list.size(); i++) {
            PrintDataItem printDataItem = list.get(i);
            List<String> cmds = printDataItem.getCmds();

            //left
            if (cmds.contains(LEFT_ALIGN) && leftCount == 0) {
                leftCount++;
                leftItem = printDataItem;
            } else if (cmds.contains(RIGHT_ALIGN) && rightCount == 0) {
                rightCount++;
                rightItem = printDataItem;
            } else if (cmds.contains(CENTER_ALIGN) && centerCount == 0) {
                centerCount++;
                centerItem = printDataItem;
            } else {
                tempList.add(printDataItem);
            }
        }

        if (leftItem != null) {
            tempList.add(0, leftItem);
        }

        if (rightItem != null) {
            tempList.add(tempList.size(), rightItem);
        }

        if (centerItem != null) {

            if (tempList.size() > 2) {
                tempList.add(tempList.size() / 2 + 1, centerItem);
            } else if (tempList.size() == 2) {
                tempList.add(tempList.size() / 2, centerItem);
            } else if (tempList.size() == 1) {
                if (leftCount != 0) {
                    tempList.add(1, centerItem);
                } else if (rightCount != 0) {
                    tempList.add(0, centerItem);
                } else {
                    tempList.add(centerItem);
                }
            } else {
                tempList.add(centerItem);
            }
        }

        if (tempList.size() == 2) {
            boolean containCenter = false, addLeft = false, addRight = false, addBold = false;

            for (PrintDataItem printDataItem : tempList) {
                if (printDataItem.getCmds().contains(CENTER_ALIGN)) {
                    containCenter = true;
                } else if (printDataItem.getCmds().contains(LEFT_ALIGN)) {
                    addRight = true;
                } else if (printDataItem.getCmds().contains(RIGHT_ALIGN)) {
                    addLeft = true;
                } else if (printDataItem.getCmds().contains(BOLD)) {
                    addBold = true;
                }
            }

            if (containCenter) {
                if (addLeft) {
                    tempList.add(0, new PrintDataItem(" ", Arrays.asList(LEFT_ALIGN)));
                }

                if (addRight || addBold) {
                    tempList.add(2, new PrintDataItem(" ", Arrays.asList(RIGHT_ALIGN)));
                }

            }

        }
        return tempList;
    }


    public static void customizeFontSize(TextView textView, PrintDataItem printDataItem) {
        float textSize = TextShowingUtils.FONT_NORMAL_SP;
        int topPadding = 0;  // Default: no padding

        float density = textView.getContext().getResources().getDisplayMetrics().density;

        if (printDataItem.getCmds().contains(PrintDataItem.BIG_FONT)) {
            // Big font (28sp): no padding needed, use as baseline
            textSize = TextShowingUtils.FONT_BIG_SP;
            topPadding = 0;
        } else if (printDataItem.getCmds().contains(PrintDataItem.SMALL_FONT)) {
            // Small font (20sp): need more padding to align with big font bottom
            textSize = TextShowingUtils.FONT_SMALL_SP;
            // Calculate required padding: (28-20) * density
            topPadding = (int) ((FONT_BIG_SP - FONT_SMALL_SP) * density);
        } else {
            // Normal font (24sp): need some padding
            textSize = TextShowingUtils.FONT_NORMAL_SP;
            // Calculate required padding: (28-24) * density
            topPadding = (int) ((FONT_BIG_SP - FONT_NORMAL_SP) * density);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        // Key: set uniform line spacing to make all TextView line heights consistent
        // Use big font line height as standard
        float standardLineHeight = FONT_BIG_SP * density * 1.3f;  // 28sp * 1.3 = 36.4sp
        float currentLineHeight = textSize * density * 1.3f;
        float extraSpacing = standardLineHeight - currentLineHeight;

        // Set line spacing: extra pixels + multiplier
        textView.setLineSpacing(extraSpacing, 1.0f);

        // Set topPadding to align small and normal font bottom with big font
        int left = textView.getPaddingLeft();
        int right = textView.getPaddingRight();
        int bottom = textView.getPaddingBottom();
        textView.setPadding(left, topPadding, right, bottom);
    }


    // overload func,add supportLineSep param
    public static List<TextView> getViewList(Context context, String data, LinearLayout.LayoutParams layoutParams, @ColorInt int color, float textSize) {
        return getViewList(context, data, layoutParams, color, textSize, false);
    }

    // impl
    public static List<TextView> getViewList(Context context, String data, LinearLayout.LayoutParams layoutParams, @ColorInt int color, float textSize, boolean supportLineSep) {
        if (TextUtils.isEmpty(data))
            data = "";

        List<TextView> textViewList = new ArrayList<>();

        try {
            PrintDataItemContainer printDataItemContainer = PrintDataConverter.parse(data, supportLineSep);

            List<PrintDataItem> parsedItems = printDataItemContainer.getPrintDataItems();

            if (supportLineSep && containsLineBreak(parsedItems)) {
                StringBuilder mergedText = new StringBuilder();
                for (PrintDataItem item : parsedItems) {
                    if (item.getCmds().contains(LINE)) {
                        mergedText.append("\n");
                    } else if (!TextUtils.isEmpty(item.getContent())) {
                        mergedText.append(item.getContent());
                    }
                }
                PrintDataItem mergedItem = new PrintDataItem(mergedText.toString(), Arrays.asList(LEFT_ALIGN));
                TextView textView = generateTextView(context, mergedItem, layoutParams, color, textSize);
                customizeFontSize(textView, mergedItem);
                textViewList.add(textView);
                return textViewList;
            }

            List<PrintDataItem> printDataItemList = filterItems(parsedItems, PrintDataItem.LEFT_ALIGN);

            for (int i = 0; i < printDataItemList.size(); i++) {

                //If item contain' cmds contain "\\n",filtered.
                if (!printDataItemList.get(i).getCmds().contains(PrintDataItem.LINE_SEP)) {
                    TextView textView = generateTextView(context, printDataItemList.get(i), layoutParams, color, textSize);
                    customizeFontSize(textView, printDataItemList.get(i));
                    textViewList.add(textView);
                    textView.setSingleLine();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return textViewList;
    }

    public static TextView generateTextView(Context context, PrintDataItem printDataItem, LinearLayout.LayoutParams layoutParams, @ColorInt int color, float textSize) {
        TextView textView = new TextView(context);
        textView.setIncludeFontPadding(false);

        List<String> cmds = printDataItem.getCmds();
        for (String cmd : cmds) {
            switch (cmd) {
                case LEFT_ALIGN:
                    layoutParams.gravity = Gravity.LEFT;
                    textView.setGravity(Gravity.LEFT);
                    break;
                case RIGHT_ALIGN:
                    layoutParams.gravity = Gravity.RIGHT;
                    textView.setGravity(Gravity.RIGHT);

                    break;

                case CENTER_ALIGN:
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    break;

                case BOLD:
                    TextPaint textPaint = textView.getPaint();
                    textPaint.setFakeBoldText(true);
                    break;

            }
        }

        textView.setLayoutParams(layoutParams);
//        textView.setTextSize(context.getResources().getDimension(R.dimen.text_size_subtitle));
        textView.setTextSize(textSize);
        textView.setText(printDataItem.getContent());
        textView.setTextColor(color);

        // Set fixed line height to big font's line height to ensure all TextViews align at bottom
        textView.setLineSpacing(0, 1.2f);  // Line spacing multiplier

        return textView;
    }

}
