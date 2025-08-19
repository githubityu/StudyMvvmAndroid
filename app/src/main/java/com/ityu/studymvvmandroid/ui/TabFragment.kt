package com.ityu.studymvvmandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.ityu.studymvvmandroid.R
import com.ityu.studymvvmandroid.base.BaseFragment
import com.ityu.studymvvmandroid.databinding.FragmentTabBinding


class TabFragment : BaseFragment<FragmentTabBinding>(FragmentTabBinding::inflate) {
    private lateinit var nestedNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 找到嵌套的 NavHostFragment
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nested_nav_host_fragment) as NavHostFragment

        // 从嵌套的 NavHostFragment 中获取 NavController
        nestedNavController = navHostFragment.navController


        // 将 BottomNavigationView 与嵌套的 NavController 连接
        binding.containerBottomNav.setupWithNavController(nestedNavController)
        setupButtonClickListeners()
    }
    private fun setupButtonClickListeners() {
//        val viewsToDestinations = mapOf(
//            R.id.btn_nav_1 to R.id.HomeFragment,
//            R.id.btn_nav_2 to R.id.RecommendFragment,
//            R.id.btn_nav_3 to R.id.HotListFragment,
//            R.id.btn_nav_5 to R.id.HomeFragment,
//            R.id.btn_nav_4 to R.id.ProfileFragment
//        )
//
//        CustomNavigationUI.setupWithNavController(
//            viewsToDestinations,
//            nestedNavController,
//            binding.containerTab
//        )


        binding.btnNav1.setOnClickListener {
            navigateTo(R.id.HomeFragment)
        }
        binding.btnNav2.setOnClickListener {
            navigateTo(R.id.RecommendFragment)
        }
        binding.btnNav3.setOnClickListener {
            navigateTo(R.id.HotListFragment)
        }
        binding.btnNav4.setOnClickListener {
            navigateTo(R.id.ProfileFragment)
        }
    }
    /**
     * 【核心】自定义的导航方法，它会配置 NavOptions 来实现 show/hide 行为。
     * @param destinationId 要导航到的 Fragment 在导航图中的 ID。
     */
    private fun navigateTo(@IdRes destId: Int) {
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true) // 恢复状态
            .setPopUpTo(
                nestedNavController.graph.startDestinationId,
                inclusive = false,
                saveState = true // 保存状态
            )
        val options = builder.build()
        try {
            // 执行导航
            nestedNavController.navigate(destId, null, options)
        } catch (e: IllegalArgumentException) {
            // 捕获当目标 destination 找不到时的异常
            e.printStackTrace()
        }
    }

}