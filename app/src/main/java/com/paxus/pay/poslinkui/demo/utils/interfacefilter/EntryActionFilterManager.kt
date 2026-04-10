package com.paxus.pay.poslinkui.demo.utils.interfacefilter

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.paxus.pay.poslinkui.demo.data.EntryActionStateStore
import com.paxus.pay.poslinkui.demo.utils.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntryActionFilterManager @Inject constructor(
    private val catalogRepository: EntryActionCatalogRepository,
    private val stateStore: EntryActionStateStore
) {

    private sealed interface StoredEntryToggle {
        object Missing : StoredEntryToggle
        data class Present(val enabled: Boolean) : StoredEntryToggle
    }

    private fun readStoredToggle(alias: String?): StoredEntryToggle {
        if (!stateStore.contains(alias)) return StoredEntryToggle.Missing
        val enabled = stateStore.get(alias) ?: return StoredEntryToggle.Missing
        return StoredEntryToggle.Present(enabled)
    }

    fun getCategories(): MutableList<EntryCategory?> {
        return catalogRepository.getCategories()
    }

    fun getStaticEntryActionListByCategory(category: String?): MutableList<String?> {
        return catalogRepository.getEntryActionsByCategoryWithDefaultValues(category)
    }

    private fun getEntryActionStateFromPM(context: Context, actionAlias: String?): Boolean {
        val componentName =
            ComponentName(context.getPackageName(), context.getPackageName() + actionAlias)
        return context.getPackageManager()
            .getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }

    /**
     * Gets current Entry Action State.
     * Syncs if necessary.
     */
    fun getSyncedEntryActionState(context: Context, action: String?): EntryAction? {
        val entryAction = catalogRepository.getEntryAction(action)
        if (entryAction == null) return null

        val isEnabled = getEntryActionStateFromPM(context, entryAction.alias)
        var shouldBeEnabled = when (val stored = readStoredToggle(entryAction.alias)) {
            is StoredEntryToggle.Present -> stored.enabled
            StoredEntryToggle.Missing -> {
                val def = entryAction.enableByDefault
                setEntryActionState(entryAction.alias, def)
                def
            }
        }

        if (isEnabled == shouldBeEnabled) {
            entryAction.isCurrentlyEnabled = isEnabled
            return entryAction
        }

        val successfullySetInPM =
            forceSetEntryActionStateInPM(context, entryAction.alias, shouldBeEnabled)
        if (!successfullySetInPM) {
            shouldBeEnabled = getEntryActionStateFromPM(context, entryAction.alias)
        }
        setEntryActionState(entryAction.alias, shouldBeEnabled)
        entryAction.isCurrentlyEnabled = shouldBeEnabled
        return entryAction
    }

    private fun setEntryActionState(alias: String?, isEnabled: Boolean) {
        stateStore.set(alias, isEnabled)
    }

    fun forceSetEntryActionStateInPM(
        context: Context,
        alias: String?,
        shouldBeEnabled: Boolean
    ): Boolean {
        var isSuccessful = false
        try {
            val componentName =
                ComponentName(context.getPackageName(), context.getPackageName() + alias)
            context.getPackageManager().setComponentEnabledSetting(
                componentName,
                if (shouldBeEnabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            isSuccessful = true
        } catch (e: RuntimeException) {
            Logger.e(e.message ?: "RuntimeException")
        }

        return isSuccessful
    }

    fun switchEntryActionState(
        context: Context,
        action: String?,
        isEnabled: Boolean,
        callback: Callback
    ) {
        val entryAction = getSyncedEntryActionState(context, action) ?: return

        //If already in the desired state, then return.
        if (isEnabled == entryAction.isCurrentlyEnabled) {
            callback.run(isEnabled)
            return
        }

        // Persist in store and then sync with PackageManager.
        setEntryActionState(entryAction.alias, isEnabled)
        val syncedEntryAction = getSyncedEntryActionState(context, action) ?: return

        callback.run(syncedEntryAction.isCurrentlyEnabled)
    }

    fun interface Callback {
        fun run(isEnabled: Boolean)
    }
}
