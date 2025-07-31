package com.ityu.studymvvmandroid.utils


import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ityu.studymvvmandroid.R

/**
 * 一个用于显示自定义对话框的工具类。
 */
object DialogUtils {

    /**
     * 显示一个自定义样式的确认对话框。
     *
     * @param context 上下文，通常是 Activity 或 Fragment.requireContext()。
     * @param title 对话框的标题。
     * @param message 对话框的主体信息。
     * @param positiveButtonText 确认按钮的文本，默认为 "Confirm"。
     * @param negativeButtonText 取消按钮的文本，默认为 "Cancel"。
     * @param onConfirm 用户点击确认按钮时要执行的回调函数。
     * @param onCancel (可选) 用户点击取消按钮时要执行的回调函数。
     */
    fun showCustomDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String? = "Confirm",
        onPositiveClick: (() -> Unit)? = null,
        negativeButtonText: String? = "Cancel",
        onNegativeClick: (() -> Unit)? = null,
        isCancelable: Boolean = true
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null)
        val builder = AlertDialog.Builder(context).setView(dialogView)
        val dialog = builder.create()

        // 获取控件
        val tvTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
        val tvMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btn_cancel)
        val btnConfirm = dialogView.findViewById<MaterialButton>(R.id.btn_confirm)

        // 设置文本
        tvTitle.text = title
        tvMessage.text = message

        // --- 核心逻辑：根据传入的参数动态显示/隐藏按钮 ---

        // 1. 处理确认按钮 (Positive Button)
        if (positiveButtonText != null && onPositiveClick != null) {
            btnConfirm.text = positiveButtonText
            btnConfirm.setOnClickListener {
                onPositiveClick.invoke()
                dialog.dismiss()
            }
            btnConfirm.visibility = View.VISIBLE
        } else {
            // 如果没有提供确认按钮的文本或回调，则隐藏它
            btnConfirm.visibility = View.GONE
        }

        // 2. 处理取消按钮 (Negative Button)
        if (negativeButtonText != null) {
            btnCancel.text = negativeButtonText
            btnCancel.setOnClickListener {
                onNegativeClick?.invoke() // onNegativeClick 本身就是可选的
                dialog.dismiss()
            }
            btnCancel.visibility = View.VISIBLE
        } else {
            // 如果没有提供取消按钮的文本，则隐藏它
            btnCancel.visibility = View.GONE
        }

        // 3. (重要) 如果只有一个按钮，让它居中
        // 通过修改 ConstraintLayout 的链式约束来实现
        if (btnConfirm.visibility == View.GONE && btnCancel.visibility == View.VISIBLE) {
            // 只有取消按钮可见
            val params =
                btnCancel.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            params.endToEnd =
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            btnCancel.layoutParams = params
        } else if (btnCancel.visibility == View.GONE && btnConfirm.visibility == View.VISIBLE) {
            // 只有确认按钮可见
            val params =
                btnConfirm.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            params.startToStart =
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            btnConfirm.layoutParams = params
        }

        // 设置对话框属性
        dialog.setCancelable(isCancelable)

        // 7. (关键) 设置对话框窗口背景为透明，以显示 CardView 的圆角
        dialog.let {
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            it.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
        }
        // 8. 显示对话框
        dialog.show()
    }


}


