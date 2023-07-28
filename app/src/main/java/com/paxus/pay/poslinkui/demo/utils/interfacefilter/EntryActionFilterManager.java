package com.paxus.pay.poslinkui.demo.utils.interfacefilter;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.paxus.pay.poslinkui.demo.utils.Logger;

import java.util.List;

public class EntryActionFilterManager {
    private static final String SharedPreferencesName = "ENTRY_ACTION_FILTER_PREFS";

    public static List<EntryCategory> getCategories(){
        return EntryActionAndCategoryRepository.getCategories();
    }

    public static List<String> getStaticEntryActionListByCategory(String category) {
        return EntryActionAndCategoryRepository.getEntryActionsByCategoryWithDefaultValues(category);
    }

    public @FunctionalInterface interface Callback {
        void run(boolean isEnabled);
    }

    private static Boolean getEntryActionStateFromSP(Context context, String actionAlias) {
        if(!context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE).contains(actionAlias)) return null;
        return context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE).getBoolean(actionAlias, false);
    }

    private static boolean getEntryActionStateFromPM(Context context, String actionAlias) {
        ComponentName componentName = new ComponentName(context.getPackageName(), context.getPackageName() + actionAlias);
        return context.getPackageManager().getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    /**
     * Gets current Entry Action State.
     * Syncs if necessary.
     */
    public static EntryAction getSyncedEntryActionState(Context context, String action) {
        EntryAction entryAction = EntryActionAndCategoryRepository.getEntryAction(action);
        if (entryAction == null) return null;

        boolean isEnabled = getEntryActionStateFromPM(context, entryAction.alias);
        Boolean shouldBeEnabled = getEntryActionStateFromSP(context, entryAction.alias);
        if(shouldBeEnabled == null) {
            shouldBeEnabled = entryAction.enableByDefault;
            setEntryActionStateInSP(context, entryAction.alias, entryAction.enableByDefault);
        }

        if(isEnabled == shouldBeEnabled) {
            entryAction.isCurrentlyEnabled = isEnabled;
            return entryAction;
        } else {
            boolean successfullySetInPM = forceSetEntryActionStateInPM(context, entryAction.alias, shouldBeEnabled);

            //If PM does not allow new values, accept what PM has.
            if(!successfullySetInPM) shouldBeEnabled = getEntryActionStateFromPM(context, entryAction.alias);

            setEntryActionStateInSP(context, entryAction.alias, shouldBeEnabled);
            entryAction.isCurrentlyEnabled = shouldBeEnabled;
            return entryAction;
        }
    }

    private static void setEntryActionStateInSP(Context context, String alias, boolean isEnabled) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(alias, isEnabled).apply();
    }

    public static boolean forceSetEntryActionStateInPM(Context context, String alias, boolean shouldBeEnabled) {
        boolean isSuccessful = false;
        try {
            ComponentName componentName = new ComponentName(context.getPackageName(), context.getPackageName() + alias);
            context.getPackageManager().setComponentEnabledSetting(componentName,
                    shouldBeEnabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            isSuccessful = true;
        } catch (RuntimeException e) {
            Logger.e(e.getMessage());
        }

        return isSuccessful;
    }

    public static void switchEntryActionState(Context context, String action, Boolean isEnabled, Callback callback) {
        EntryAction entryAction = getSyncedEntryActionState(context, action);

        //If already in the desired state, then return.
        if(isEnabled == entryAction.isCurrentlyEnabled){
            callback.run(isEnabled);
            return;
        }

        //Set in Shared Preferences. And Sync in PM.
        setEntryActionStateInSP(context, entryAction.alias, isEnabled);
        EntryAction syncedEntryAction = getSyncedEntryActionState(context, action);

        callback.run(syncedEntryAction.isCurrentlyEnabled);
    }

}
