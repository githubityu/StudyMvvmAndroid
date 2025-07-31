package com.ityu.studymvvmandroid.utils


import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 一个用于处理主题、状态栏和导航栏的工具类。
 * 使用现代的 WindowInsetsControllerCompat API，自动处理版本兼容性。
 */
object Theme2Utils {
    fun hideSystemUI(window: Window) {
        // 使用现代的 WindowInsetsController API (Android 11+, API 30+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 告诉系统我们的 App 会自己处理 Insets，从而实现 edge-to-edge
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowCompat.getInsetsController(window, window.decorView)
            // 隐藏状态栏和导航栏
            controller.hide(WindowInsetsCompat.Type.systemBars())
            // 设置当用户从屏幕边缘划入时，系统栏短暂显示，然后自动隐藏
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            // 使用旧版的 setSystemUiVisibility API (适用于 Android 6.0 等旧版本)
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    // 视图延伸到导航栏后面
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // 隐藏导航栏
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            // 隐藏状态栏
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            // 实现“粘性沉浸式”，用户划出系统栏后，过几秒会自动隐藏
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // 保持布局稳定，防止因系统栏隐藏/显示而跳动
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }
    }

    fun showSystemUI(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 告诉系统恢复默认行为，由系统处理 Insets
            WindowCompat.setDecorFitsSystemWindows(window, true)
            val controller = WindowCompat.getInsetsController(window, window.decorView)
            // 显示状态栏和导航栏
            controller.show(WindowInsetsCompat.Type.systemBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    // 清除之前的标志位，恢复布局
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

}