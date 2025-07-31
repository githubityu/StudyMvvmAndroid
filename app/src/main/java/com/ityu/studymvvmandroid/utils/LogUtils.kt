package com.ityu.studymvvmandroid.utils

import timber.log.Timber

object LogUtils {

    // Initialize Timber in your Application class (see step 3)

    fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    fun i(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }

    fun w(tag: String, message: String) {
        Timber.tag(tag).w(message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Timber.tag(tag).e(throwable, message)
    }

    fun v(tag:String, message: String){
        Timber.tag(tag).v(message)
    }
}