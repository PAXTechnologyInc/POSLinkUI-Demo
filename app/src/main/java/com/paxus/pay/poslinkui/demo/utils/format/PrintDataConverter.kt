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
package com.paxus.pay.poslinkui.demo.utils.format

import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is used to convert printData string to bitmap.
 */
@Singleton
class PrintDataConverter @Inject constructor() {
    //    public static final String SINGLE_ONLY_CONTROLLER_LIST = "SINGLE_ONLY_CONTROLLER_LIST";
    //    public static final String DESCRIPTION_CONTROLLER_LIST = "DESCRIPTION_CONTROLLER_LIST";
    //    public static final String CONTENT_CONTROLLER_LIST = "CONTENT_CONTROLLER_LIST";
    //    public static final String BARCODE_LIST = "BARCODE_LIST";
    val TEXT_SHOWING_LIST: String = "TEXT_SHOWING_LIST"

    /**
     * Parse the raw printData string to PrintDataItems.
     * 
     * @param printData
     * @return
     */
    fun parse(printData: String, supportLineSep: Boolean): PrintDataItemContainer {
        val defaultControllerKeyToRecognizers: Map<String, List<String?>?> = mapOf(
            TEXT_SHOWING_LIST to PrintDataItem.TEXT_SHOWING_LIST
        )
        return parse(printData, defaultControllerKeyToRecognizers, supportLineSep)
    }

    fun parse(
        printData: String,
        controllerKeyToRecognizers: Map<String, List<String?>?>,
        supportLineSep: Boolean
    ): PrintDataItemContainer {
        val printDataItemList: MutableList<PrintDataItem?> = ArrayList(32)
        val recognizer = StringBuilder()
        val contentBuilder = StringBuilder()
        val cmds: MutableList<String?> = ArrayList()
        for (index in 0 until printData.length) {
            val curChar = printData[index]
            processChar(curChar, supportLineSep, recognizer, contentBuilder, printDataItemList, cmds)
            processRecognizerMatch(
                recognizer,
                controllerKeyToRecognizers,
                printDataItemList,
                contentBuilder,
                cmds
            )
        }
        flushRecognizerToContent(recognizer, contentBuilder)
        addNewPrintItemWhenHaveContent(printDataItemList, contentBuilder, cmds)
        return PrintDataItemContainer(printDataItemList)
    }

    private fun processChar(
        curChar: Char,
        supportLineSep: Boolean,
        recognizer: StringBuilder,
        contentBuilder: StringBuilder,
        printDataItemList: MutableCollection<PrintDataItem?>,
        cmds: MutableList<String?>
    ) {
        when {
            curChar == '\\' -> handleBackslash(recognizer, contentBuilder)
            curChar == '\n' -> handleParseLineSep(printDataItemList, recognizer, contentBuilder, cmds)
            supportLineSep && isBackslashN(curChar, recognizer) -> handleBackslashN(recognizer, contentBuilder)
            else -> appendToRecognizerOrContent(curChar, recognizer, contentBuilder)
        }
    }

    private fun handleBackslash(recognizer: StringBuilder, contentBuilder: StringBuilder) {
        if (recognizer.isNotEmpty()) {
            contentBuilder.append(recognizer.toString())
            recognizer.clear()
        }
        recognizer.append('\\')
    }

    private fun isBackslashN(curChar: Char, recognizer: StringBuilder): Boolean =
        curChar == 'n' && recognizer.isNotEmpty() && recognizer[recognizer.length - 1] == '\\'

    private fun handleBackslashN(recognizer: StringBuilder, contentBuilder: StringBuilder) {
        recognizer.deleteCharAt(recognizer.length - 1)
        if (recognizer.isNotEmpty()) {
            contentBuilder.append(recognizer.toString())
            recognizer.clear()
        }
        contentBuilder.append("\n")
    }

    private fun appendToRecognizerOrContent(curChar: Char, recognizer: StringBuilder, contentBuilder: StringBuilder) {
        if (recognizer.isNotEmpty()) {
            recognizer.append(curChar)
        } else {
            contentBuilder.append(curChar)
        }
    }

    private fun processRecognizerMatch(
        recognizer: StringBuilder,
        controllerKeyToRecognizers: Map<String, List<String?>?>,
        printDataItemList: MutableCollection<PrintDataItem?>,
        contentBuilder: StringBuilder,
        cmds: MutableList<String?>
    ) {
        val recognizerStr = recognizer.toString()
        for (key in controllerKeyToRecognizers.keys) {
            val list = controllerKeyToRecognizers[key] ?: continue
            if (list.contains(recognizerStr) && key == TEXT_SHOWING_LIST) {
                addNewPrintItemWhenHaveContent(printDataItemList, contentBuilder, cmds)
                addNewCmds(cmds, recognizerStr)
                recognizer.clear()
                break
            }
        }
    }

    private fun flushRecognizerToContent(recognizer: StringBuilder, contentBuilder: StringBuilder) {
        if (recognizer.isNotEmpty()) {
            contentBuilder.append(recognizer.toString())
            recognizer.clear()
        }
    }

    private fun handleParseLineSep(
        printDataItemList: MutableCollection<PrintDataItem?>,
        recognizer: StringBuilder,
        contentBuilder: StringBuilder,
        cmds: MutableList<String?>
    ) {
        if (recognizer.isNotEmpty()) {
            contentBuilder.append(recognizer.toString())
            recognizer.delete(0, recognizer.length)
        }

        // put the fetched content to a new item
        addNewPrintItemWhenHaveContent(printDataItemList, contentBuilder, cmds)
        addPrintItmWithOneCmd(printDataItemList, contentBuilder, PrintDataItem.Companion.LINE)
        cmds.clear() //make the last cmd not affect new line text
    }

    private fun addNewCmds(cmds: MutableCollection<String?>, recognizerStr: String?) {
        cmds.add(recognizerStr)
    }

    private fun addPrintItmWithOneCmd(
        printDataItemList: MutableCollection<PrintDataItem?>,
        contentBuilder: StringBuilder,
        cmd: String?
    ) {
        printDataItemList.add(PrintDataItem(contentBuilder.toString(), mutableListOf<String?>(cmd)))
    }

    private fun addNewPrintItemWhenHaveContent(
        printDataItemList: MutableCollection<PrintDataItem?>,
        contentBuilder: StringBuilder,
        cmds: MutableList<String?>
    ) {
        if (contentBuilder.isNotEmpty()) {
            printDataItemList.add(PrintDataItem(contentBuilder.toString(), cmds))
            cmds.clear()
            contentBuilder.delete(0, contentBuilder.length)
        }
    }
}
