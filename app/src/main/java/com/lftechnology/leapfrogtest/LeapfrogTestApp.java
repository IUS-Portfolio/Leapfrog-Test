package com.lftechnology.leapfrogtest;

import android.app.Application;

import timber.log.Timber;

public class LeapfrogTestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeLogging();
    }

    private void initializeLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // TODO: Log errors in Crashlytics or Google Crash Reporting
        }
    }
}
