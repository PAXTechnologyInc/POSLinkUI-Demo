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

package com.paxus.pay.poslinkui.demo.utils.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is used to convert printData string to bitmap.
 */
public class PrintDataConverter {
//    public static final String SINGLE_ONLY_CONTROLLER_LIST = "SINGLE_ONLY_CONTROLLER_LIST";
//    public static final String DESCRIPTION_CONTROLLER_LIST = "DESCRIPTION_CONTROLLER_LIST";
//    public static final String CONTENT_CONTROLLER_LIST = "CONTENT_CONTROLLER_LIST";
//    public static final String BARCODE_LIST = "BARCODE_LIST";
    public static final String TEXT_SHOWING_LIST = "TEXT_SHOWING_LIST";

    /**
     * Parse the raw printData string to PrintDataItems.
     *
     * @param printData
     * @return
     */
    public static PrintDataItemContainer parse(final String printData, boolean supportLineSep) {

        Map<String, List<String>> defaultList = new HashMap<String, List<String>>() {
            {
                put(PrintDataConverter.TEXT_SHOWING_LIST, PrintDataItem.TEXT_SHOWING_LIST);
            }
        };
        return parse(printData, defaultList, supportLineSep);
    }

    public static PrintDataItemContainer parse(final String printData, Map<String, List<String>> filterList, boolean supportLineSep)  {

        List<PrintDataItem> printDataItemList = new ArrayList<>(32);
        StringBuilder recognizer = new StringBuilder();
        StringBuilder contentBuilder = new StringBuilder();
        List<String> cmds = new ArrayList<>();
        for (int index = 0; index < printData.length(); index++) {
            char curChar = printData.charAt(index);
            if (curChar == '\\') {
                // recognizer get \ but it is not a command --> it should be content
                if (recognizer.length() > 0) {
                    contentBuilder.append(recognizer.toString());
                    recognizer.delete(0, recognizer.length());
                }
                recognizer.append(curChar);
            } else if (curChar == '\n') {
                handleParseLineSep(printDataItemList, recognizer, contentBuilder, cmds);
            } else if (supportLineSep && curChar == 'n' && recognizer.length() > 0
                    && recognizer.charAt(recognizer.length() - 1) == '\\') {
                recognizer.delete(recognizer.length() - 1, recognizer.length());
                handleParseLineSep(printDataItemList, recognizer, contentBuilder, cmds);
            }  else {
                if (recognizer.length() > 0) {
                    // Start recognize
                    recognizer.append(curChar);
                } else {
                    contentBuilder.append(curChar);
                }
            }

            String recognizerStr = recognizer.toString();
            for (String key : filterList.keySet()) {
                if (filterList.get(key).contains(recognizerStr)) {
                    switch (key) {
                        case TEXT_SHOWING_LIST:
                            addNewPrintItemWhenHaveContent(printDataItemList, contentBuilder, cmds);
                            addNewCmds(cmds, recognizerStr);
                            recognizer.delete(0, recognizer.length());
                            break;

                    }
                }
            }
        }

        // Add the last item.
        if (recognizer.length() > 0) {
            contentBuilder.append(recognizer.toString());
            recognizer.delete(0, recognizer.length());
        }
        addNewPrintItemWhenHaveContent(printDataItemList, contentBuilder, cmds);


        return new PrintDataItemContainer(printDataItemList);
    }

    private static void handleParseLineSep(List<PrintDataItem> printDataItemList, StringBuilder recognizer, StringBuilder contentBuilder, List<String> cmds) {

        if (recognizer.length() > 0) {
            contentBuilder.append(recognizer.toString());
            recognizer.delete(0, recognizer.length());
        }

        // put the fetched content to a new item
        addNewPrintItemWhenHaveContent(printDataItemList, contentBuilder, cmds);
        addPrintItmWithOneCmd(printDataItemList, contentBuilder, PrintDataItem.LINE);
        cmds.clear(); //make the last cmd not affect new line text
    }

    private static void addNewCmds(List<String> cmds, String recognizerStr) {
        cmds.add(recognizerStr);
    }

    private static void addPrintItmWithOneCmd(List<PrintDataItem> printDataItemList, StringBuilder contentBuilder, String cmd) {
        printDataItemList.add(new PrintDataItem(contentBuilder.toString(), Collections.singletonList(cmd)));
    }

    private static void addNewPrintItemWhenHaveContent(List<PrintDataItem> printDataItemList, StringBuilder contentBuilder, List<String> cmds) {
        if (contentBuilder.length() > 0) {
            contentBuilder.length();
            printDataItemList.add(new PrintDataItem(contentBuilder.toString(), cmds));
            cmds.clear();
            contentBuilder.delete(0, contentBuilder.length());
        }
    }


}
