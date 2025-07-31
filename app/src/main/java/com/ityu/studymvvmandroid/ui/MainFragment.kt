package com.ityu.studymvvmandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ityu.studymvvmandroid.R

import com.ityu.studymvvmandroid.base.KeepStateBaseFragmentV2
import com.ityu.studymvvmandroid.databinding.FragmentTabBinding
import com.ityu.studymvvmandroid.utils.ToolbarController

// 关键：修改继承的基类

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
// 1. 修改继承的基类为 KeepStateBaseFragment
class MainFragment : KeepStateBaseFragmentV2<FragmentTabBinding>(FragmentTabBinding::inflate) {


    /**
     * 这是 KeepStateBaseFragment 提供的核心方法。
     * 所有只需要执行一次的初始化代码都应该放在这里。
     * 它会在视图首次创建后被安全地调用一次。
     */
    @SuppressLint("SetTextI18n")
    override fun onViewReady(savedInstanceState: Bundle?) {
        super.onViewReady(savedInstanceState)
        // 找到嵌套的 NavHostFragment
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nested_nav_host_fragment) as NavHostFragment

        // 从嵌套的 NavHostFragment 中获取 NavController
        val nestedNavController = navHostFragment.navController

        withBinding {
            it.containerBottomNav.setupWithNavController(nestedNavController)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            setupToolbarContent()
        }
    }

    /**
     * 当 Fragment 从隐藏变为显示时，此方法会被调用。
     * 非常适合用来刷新那些可能会过时的UI元素，比如时间。
     */


    // --------------------------------------------------------------------
    // 以下所有的私有辅助方法都不需要任何改动，因为它们的逻辑是正确的。
    // 我们只是改变了调用它们的地方。
    // --------------------------------------------------------------------


    private fun setupToolbarContent() {
        (activity as? ToolbarController)?.apply {
            setLeft("主界面", null, null)
            setRight("goTabFragment", onClick = {
                findNavController().navigate(R.id.action_MainFragment_to_TabFragment)
            })
        }
    }
}