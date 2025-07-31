package com.ityu.studymvvmandroid.utils

import android.app.Activity
import android.content.Context
import com.ityu.studymvvmandroid.view.LoadingDialog
import java.lang.ref.WeakReference

/**
 * 一个用于显示和隐藏全局加载对话框的工具类。
 * 它使用 WeakReference 来避免内存泄漏。
 */
object LoadingUtils {

    private var loadingDialogRef: WeakReference<LoadingDialog>? = null

    /**
     * 显示加载对话框。
     *
     * @param context 通常是一个 Activity 上下文。
     * @param message 可选的加载文本。
     */
    fun show(context: Context, message: String? = null) {
        // 如果上下文是 Activity 且已销毁，则不执行任何操作
        if (context is Activity && context.isFinishing) {
            return
        }

        // 如果当前有对话框正在显示，先隐藏它
        hide()

        // 创建新的对话框实例
        val dialog = LoadingDialog(context).apply {
            setMessage(message) // 设置消息
        }

        // 显示对话框
        dialog.show()

        // 保存对对话框的弱引用
        loadingDialogRef = WeakReference(dialog)
    }

    /**
     * 隐藏当前正在显示的加载对话框。
     */
    fun hide() {
        loadingDialogRef?.get()?.let { dialog ->
            if (dialog.isShowing) {
                try {
                    dialog.dismiss()
                } catch (e: Exception) {
                    // 忽略可能在Activity销毁时发生的异常
                }
            }
        }
        loadingDialogRef?.clear()
        loadingDialogRef = null
    }
}