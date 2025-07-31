package com.ityu.studymvvmandroid.utils // 或者放在一个更核心的包，如 com.ityu.studymvvmandroid.app

import android.content.Context

/**
 * 一个线程安全的、用于在应用全局范围内提供 ApplicationContext 的单例对象。
 *
 * 使用方式:
 * 1. 在 Application.onCreate() 中调用 init() 方法进行初始化。
 * 2. 在代码的任何地方通过 AppContextProvider.getContext() 来安全地获取上下文。
 */
object AppContextProvider {

    @Volatile
    private var appContext: Context? = null
    private val TAG = "AppContextProvider"

    /**
     * 初始化 Provider。必须在 Application.onCreate() 中调用一次。
     * @param context 必须是 ApplicationContext，以防止内存泄漏。
     */
    fun init(context: Context) {
        if (appContext != null) {
            return
        }
        // 使用 applicationContext 确保我们持有的是应用的上下文，而不是某个 Activity 的
        this.appContext = context.applicationContext
    }

    /**
     * 获取 ApplicationContext。
     * @return 返回已初始化的 ApplicationContext。
     * @throws IllegalStateException 如果 init() 方法还未被调用。
     */
    fun getContext(): Context {
        return appContext ?: throw IllegalStateException("AppContextProvider has not been initialized. Call init() in your Application class.")
    }
}