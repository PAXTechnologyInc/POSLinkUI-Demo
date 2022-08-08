package com.paxus.pay.poslinkui.demo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Yanina.Yang on 5/13/2022.
 *
 * Add a log on Android HOME for easy install&uninstall
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String appVer = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            ((TextView)findViewById(R.id.version)).setText("Version:"+appVer);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
