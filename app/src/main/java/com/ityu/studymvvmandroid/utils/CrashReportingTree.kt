package com.ityu.studymvvmandroid.utils
import android.util.Log
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return // Don't log VERBOSE or DEBUG messages to Crashlytics
        }
        if (priority == Log.ERROR || priority == Log.WARN) {
            LogUtils.e("CrashReportingTree",message)  // Log message to Crashlytics

        }
    }
}