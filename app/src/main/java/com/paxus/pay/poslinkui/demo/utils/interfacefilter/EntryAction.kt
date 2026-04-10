package com.paxus.pay.poslinkui.demo.utils.interfacefilter

class EntryAction(
    var category: String?,
    var action: String?,
    var name: String?,
    var alias: String?,
    var enableByDefault: Boolean
) {
    var isCurrentlyEnabled: Boolean = false
}