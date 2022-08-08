package com.paxus.pay.poslinkui.demo;

import android.app.Application;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.paxus.pay.poslinkui.demo.utils.FileLogAdapter;

/**
 * Created by Yanina.Yang on 5/11/2022.
 *
 * Init log when application started
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //============Init Log===========

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(1)
                .methodOffset(6)        // (Optional) Hides internal method calls up to offset. Default 0
                .tag("POSLinkUIDemo")
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new FileLogAdapter(formatStrategy,
                getExternalFilesDir(null).getAbsolutePath(), "POSLinkUILog"));

    }
}
