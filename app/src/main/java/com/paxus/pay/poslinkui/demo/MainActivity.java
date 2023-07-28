package com.paxus.pay.poslinkui.demo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.paxus.pay.poslinkui.demo.settings.EntryActionAndCategoryFilterFragment;

/**
 * Created by Yanina.Yang on 5/13/2022.
 * <p>
 * Add a log on Android HOME for easy install&uninstall
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String appVer = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            ((TextView) findViewById(R.id.version)).setText("Version: " + appVer);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        findViewById(R.id.customize_filter).setOnClickListener(view -> {
            getSupportFragmentManager().executePendingTransactions();
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(EntryActionAndCategoryFilterFragment.class.getSimpleName())
                    .setCustomAnimations(R.anim.anim_enter_from_bottom, R.anim.anim_exit_to_bottom)
                    .replace(R.id.activity_main_fragment_container, new EntryActionAndCategoryFilterFragment()).commit();
            ((CheckedTextView)view).setChecked(true);
        });

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDetached(fm, f);
                ((CheckedTextView)findViewById(R.id.customize_filter)).setChecked(false);
            }
        }, false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
