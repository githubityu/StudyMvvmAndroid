package com.ityu.studymvvmandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ityu.studymvvmandroid.base.BaseFragment
import com.ityu.studymvvmandroid.databinding.FragmentFirstBinding
import com.ityu.studymvvmandroid.domain.model.SharedViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    private val sharedViewModel: SharedViewModel by activityViewModels() // Use activityViewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val myArgValue = "Hello from FirstFragment"
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(myArgValue)
            findNavController().navigate(action)
        }
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            // Update UI based on shared data
            binding.buttonSecond.text = data
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 在这里处理返回键事件
                showExitConfirmationDialog()
            }
        }

        // 2. 将回调添加到 OnBackPressedDispatcher
        // requireActivity().onBackPressedDispatcher 会获取到宿主 Activity 的分发器
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("确认退出")
            .setMessage("您确定要离开此页面吗？未保存的更改将会丢失。")
            .setPositiveButton("确定") { _, _ ->
                // 如果用户点击“确定”，我们需要先禁用当前的回调，然后再执行返回操作
                // 否则会陷入无限循环！

                // 禁用回调
                // this.isEnabled = false // 在 OnBackPressedCallback 内部，`this` 指向 callback 自身
                // 执行真正的返回操作
                // requireActivity().onBackPressed()

                // 更简洁的做法是直接调用 navigateUp() 或 popBackStack()
                findNavController().popBackStack()
            }
            .setNegativeButton("取消", null) // 点击“取消”则什么都不做
            .show()
    }


}