package com.ityu.studymvvmandroid.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

// ...
interface Diffable {
    val diffId: Any
}

class GenericDiffCallback<T : Diffable> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        // This is CORRECT. We are checking if two items represent the same
        // logical entity (e.g., user with a specific ID).
        return oldItem.diffId == newItem.diffId
    }

    /**
     * This is INCORRECT and the source of the warning.
     */
    @SuppressLint("DiffUtilEquals") // 如果你确定 T 总是 data class 或正确实现了 equals，可以加上这个注解来抑制警告
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem // 使用 '=='，在Kotlin中它会调用 equals()
    }
}