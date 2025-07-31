package com.ityu.studymvvmandroid.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.ityu.studymvvmandroid.R

class LoadingDialog(context: Context) : Dialog(context, R.style.LoadingDialogTheme) { // 推荐使用一个主题

    private var loadingTextView: TextView? = null
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        loadingTextView = findViewById(R.id.loading_text)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }


    // ... setMessage, updateMessageView, show 方法保持不变 ...
    fun setMessage(messageText: String?): LoadingDialog {
        this.message = messageText
        if (isShowing) {
            updateMessageView()
        }
        return this
    }

    private fun updateMessageView() {
        if (!message.isNullOrEmpty()) {
            loadingTextView?.text = message
            loadingTextView?.visibility = View.VISIBLE
        } else {
            loadingTextView?.visibility = View.GONE
        }
    }

    override fun show() {
        // 在 show() 之前确保窗口已准备好
        if (window == null) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        super.show()
        updateMessageView()
    }
}