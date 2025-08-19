package com.ityu.studymvvmandroid.base

// KeepStateNavHostFragment.kt
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign

class KeepStateNavHostFragment : NavHostFragment() {
    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)

        // 1. 创建我们的自定义 Navigator
        val keepStateNavigator = KeepStateFragmentNavigator(
            requireContext(),
            childFragmentManager,
            id // containerId, 即 NavHostFragment 在布局中的 ID
        )

        // 2. 将自定义 Navigator 添加到 NavigatorProvider 中
        navHostController.navigatorProvider += keepStateNavigator
    }
}