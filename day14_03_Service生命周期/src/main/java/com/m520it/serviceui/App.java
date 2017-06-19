package com.m520it.serviceui;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author 蓝兵
 * @time 2017/3/1  18:22
 */
public class App extends Application {

    public static RefWatcher sWatcher;

    private static void initLeakCanary(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return;
        }
        sWatcher = LeakCanary.install(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary(this);
    }
}
