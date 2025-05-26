/*
 * ============================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 *
 * Module Date: 2019/7/30
 * Module Auth: Frank.W
 * Description:
 *
 * Revision History:
 * Date                   Author                       Action
 * 2019/7/30              Frank.W                       Create
 * ============================================================================
 */

package com.paxus.pay.poslinkui.demo.entry.poslink;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.paxus.pay.poslinkui.demo.utils.format.PrintDataConverter;
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataItem;
import com.paxus.pay.poslinkui.demo.utils.format.PrintDataItemContainer;

import java.util.ArrayList;
import java.util.List;

public class ShowTextBoxController {
    public static int line;//记录行数

    private Context context;

    private ShowTextBoxController(Context context) {
        this.context = context;
    }

    private @ColorInt int defaultColor = Color.BLACK;
    private String defaultAlignment = PrintDataItem.LEFT_ALIGN;

    public static ShowTextBoxController fromContext(Context context) {
        return new ShowTextBoxController(context);
    }

    public ShowTextBoxController defaultColor(@ColorInt int color) {
        this.defaultColor = color;
        return this;
    }

    public ShowTextBoxController defaultAlignment(String alignment) {
        this.defaultAlignment = alignment;
        return this;
    }

    public void showText(LinearLayout containerLayout, String data, LinearLayout.LayoutParams lp) {
        line = 0;
        List<PrintDataItem> newLineTextList = new ArrayList<>();
        if (lp == null) {
            lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        }

        if (TextUtils.isEmpty(data))
            data = "";

        data = data.replaceAll("\r", ""); //BPOSANDJAX-264
        try {
            PrintDataItemContainer printDataItemContainer = PrintDataConverter.parse(data, true);
            List<PrintDataItem> printDataItemList = printDataItemContainer.getPrintDataItems();

            for (int i = 0; i < printDataItemList.size(); i++) {
                PrintDataItem currDataItem = printDataItemList.get(i);
                PrintDataItem preDataItem = null;
                if (i >= 1) {
                    preDataItem = printDataItemList.get(i - 1);
                }
                if (currDataItem.getCmds().contains(PrintDataItem.LINE)
                        || currDataItem.getContent().contains("\\r")
                        || currDataItem.getCmds().contains(PrintDataItem.LINE_SEP)) {
                    if (newLineTextList.size() == 0) {
                        newLineTextList.add(currDataItem);//输出空白行
                    }
                    newLineTextList = TextShowingUtils.filterItems(newLineTextList, defaultAlignment);
                    showTextInOnLine(containerLayout, newLineTextList, lp);
                    newLineTextList.clear();
                } else if (currDataItem.getCmds().contains(PrintDataItem.BOLD)
                        || currDataItem.getCmds().contains(PrintDataItem.SMALL_FONT)
                        || currDataItem.getCmds().contains(PrintDataItem.NORMAL_FONT)
                        || currDataItem.getCmds().contains(PrintDataItem.BIG_FONT)) {
                    if (preDataItem != null) {
                        if (preDataItem.getCmds().contains(PrintDataItem.LINE)
                                || preDataItem.getContent().contains("\\r")
                                || preDataItem.getCmds().contains(PrintDataItem.LINE_SEP)) {
                            newLineTextList.add(currDataItem);
                        } else {
                            newLineTextList = TextShowingUtils.filterItems(newLineTextList, defaultAlignment);
                            showTextInOnLine(containerLayout, newLineTextList, lp);
                            newLineTextList.clear();
                            newLineTextList.add(currDataItem);
                        }
                    } else {
                        newLineTextList.add(currDataItem);
                    }
                } else {
                    newLineTextList.add(currDataItem);
                }
            }
            if (newLineTextList.size() > 0) {
                newLineTextList = TextShowingUtils.filterItems(newLineTextList, defaultAlignment);
                showTextInOnLine(containerLayout, newLineTextList, lp);
                newLineTextList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Item doesn't contains '\n'、'\\n' or '\r'
    private void showTextInOnLine(LinearLayout containerLayout, List<PrintDataItem> printDataItemList, LinearLayout.LayoutParams lp) {
        line++;
        printDataItemList = TextShowingUtils.sortList(printDataItemList);
        LinearLayout oneLineLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        oneLineLayout.setLayoutParams(params);
        float textSize = TextShowingUtils.FONT_NORMAL_SP;
        for (int i = 0; i < printDataItemList.size(); i++) {
            PrintDataItem item = printDataItemList.get(i);
            if (item.getCmds().contains(PrintDataItem.BIG_FONT)) {
                textSize = TextShowingUtils.FONT_BIG_SP;
                break;
            } else if (item.getCmds().contains(PrintDataItem.SMALL_FONT)) {
                textSize = TextShowingUtils.FONT_SMALL_SP;
                break;
            }
        }
        for (int i = 0; i < printDataItemList.size(); i++) {
            PrintDataItem item = printDataItemList.get(i);
            TextView textView = TextShowingUtils.generateTextView(context, item, lp, defaultColor, textSize);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            oneLineLayout.addView(textView, lp);
        }
        int h = oneLineLayout.getHeight();
        containerLayout.addView(oneLineLayout);
        printDataItemList.clear();
    }
}
