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

/**
 * Created by Leon.F on 2018/3/29.
 */
class PrintDataItemContainer(items: MutableList<PrintDataItem?>) {
    val printDataItems: MutableList<PrintDataItem?> = ArrayList<PrintDataItem?>()

    init {
        printDataItems.addAll(items)
    }

    override fun toString(): String {
        return super.toString() + ", size=" + printDataItems.size
    }
}
