package com.ityu.studymvvmandroid.di

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 一个应用级别的单例，用于管理全局状态，如此处的启动流程状态。
 */
@Singleton
class AppStatusManager @Inject constructor() {
    private val _isAppReady = MutableStateFlow(false)
    val isAppReady = _isAppReady.asStateFlow()

    fun setAppReady(isReady: Boolean) {
        _isAppReady.value = isReady
    }
}