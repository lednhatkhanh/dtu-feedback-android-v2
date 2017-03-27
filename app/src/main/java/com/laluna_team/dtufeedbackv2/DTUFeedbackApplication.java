package com.laluna_team.dtufeedbackv2;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lednh on 3/27/2017.
 */

public class DTUFeedbackApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
