package com.ityu.studymvvmandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ityu.studymvvmandroid.databinding.ItemUserBinding
import com.ityu.studymvvmandroid.domain.model.User

// RecyclerView Adapter
class UserAdapter(
    private val onItemClick: (User) -> Unit
) : ListAdapter<User, UserViewHolder>(GenericDiffCallback<User>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick // 将点击监听器传递给 ViewHolder
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

open class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onItemClick: (User) -> Unit // 接收 Item 点击监听器
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.apply {
            userName.text = user.name
            userEmail.text = user.email

            // 设置 Item 点击监听器
            root.setOnClickListener {
                onItemClick(user) // 在点击时调用监听器，并将 User 对象传递给它
            }
        }
    }
}