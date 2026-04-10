package com.paxus.pay.poslinkui.demo.entry;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.lang.reflect.Constructor;

/**
 * Minimal {@link Resources} for local JVM unit tests: avoids real string tables while supporting
 * both {@code getString(int)} and {@code getString(int, Object...)} (Kotlin {@code vararg} overrides
 * can miss the varargs JVM bridge).
 */
@SuppressWarnings("deprecation")
public final class StubTestResources extends Resources {

    public StubTestResources() throws Exception {
        super(newAssetManager(), new DisplayMetrics(), new Configuration());
    }

    private static AssetManager newAssetManager() throws Exception {
        Constructor<AssetManager> c = AssetManager.class.getDeclaredConstructor();
        c.setAccessible(true);
        return c.newInstance();
    }

    @Override
    public CharSequence getText(int id) {
        return getString(id);
    }

    @Override
    public String getString(int id) {
        return "stub_res_" + id;
    }

    @Override
    public String getString(int id, Object... formatArgs) {
        if (formatArgs == null || formatArgs.length == 0) {
            return getString(id);
        }
        StringBuilder b = new StringBuilder(getString(id));
        b.append("_");
        for (Object o : formatArgs) {
            b.append(o != null ? o.toString() : "null").append(",");
        }
        return b.toString();
    }
}
