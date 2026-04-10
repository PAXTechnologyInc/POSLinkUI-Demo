package com.paxus.pay.poslinkui.demo.data

/**
 * Persists customized on/off state for entry action aliases.
 */
interface EntryActionStateStore {
    fun contains(alias: String?): Boolean
    fun get(alias: String?): Boolean?
    fun set(alias: String?, enabled: Boolean)
}
