package com.ityu.studymvvmandroid

import android.app.Application
import com.ityu.studymvvmandroid.utils.AppContextProvider
import com.ityu.studymvvmandroid.utils.CrashReportingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextProvider.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) // Plant DebugTree in debug builds
        } else {
            Timber.plant(CrashReportingTree()) // Plant CrashReportingTree in release builds
        }
    }
}