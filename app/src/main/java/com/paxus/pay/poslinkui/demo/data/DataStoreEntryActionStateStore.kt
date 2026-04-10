package com.paxus.pay.poslinkui.demo.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val ENTRY_ACTION_FILTER_PREFERENCES = "entry_action_filter_prefs"
private val Context.entryActionDataStore by preferencesDataStore(name = ENTRY_ACTION_FILTER_PREFERENCES)

/**
 * DataStore-backed state storage for Entry action filtering.
 */
@Singleton
class DataStoreEntryActionStateStore @Inject constructor(
    @ApplicationContext private val context: Context
) : EntryActionStateStore {

    override fun contains(alias: String?): Boolean {
        if (alias.isNullOrBlank()) return false
        val key = keyFor(alias)
        return readPreferences().contains(key)
    }

    override fun get(alias: String?): Boolean? {
        if (alias.isNullOrBlank()) return null
        val key = keyFor(alias)
        return readPreferences()[key]
    }

    override fun set(alias: String?, enabled: Boolean) {
        if (alias.isNullOrBlank()) return
        val key = keyFor(alias)
        runBlocking {
            context.entryActionDataStore.edit { prefs ->
                prefs[key] = enabled
            }
        }
    }

    private fun readPreferences(): Preferences = runBlocking { context.entryActionDataStore.data.first() }

    private fun keyFor(alias: String): Preferences.Key<Boolean> {
        return booleanPreferencesKey(alias)
    }
}
