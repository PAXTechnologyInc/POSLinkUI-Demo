package com.paxus.pay.poslinkui.demo.entry.text.fleet

/**
 * ClassName:FleetData
 * Description:
 * date:2025/12/8
 */
class FleetData(
    var titleId: Int,
    var defaultValuePattern: String?,
    var requestedParamName: String?,
    var defaultInputType: String?
) {
    fun setTitle(titleId: Int) {
        this.titleId = titleId
    }
}
