package com.ityu.studymvvmandroid.utils

import android.view.View
import androidx.annotation.DrawableRes

interface ToolbarController {
    /** 设置左侧元素的内容和样式 */
    fun setLeft(text: String?, @DrawableRes iconResId: Int? = null, onClick: ((View) -> Unit)? = null)

    /** 设置右侧元素的内容和样式 */
    fun setRight(text: String?, @DrawableRes iconResId: Int? = null, onClick: ((View) -> Unit)? = null)

    /** 设置中间标题的内容 */
    fun setTitle(text: String?)
}