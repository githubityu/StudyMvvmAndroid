package com.ityu.studymvvmandroid.utils // 或者你喜欢的任何包名

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import java.lang.ref.WeakReference

/**
 * 我们自定义的 NavigationUI 辅助对象。
 */
object CustomNavigationUI {

    /**
     * 将一组自定义视图（如 Buttons）与 NavController 连接起来。
     *
     * @param viewsToDestinations 一个 Map，Key 是视图的 ID，Value 是导航图中对应的 destination ID。
     * @param navController 用于导航的 NavController。
     * @param viewGroup 包含这些导航视图的父布局（例如 LinearLayout）。
     */
    fun setupWithNavController(
        viewsToDestinations: Map<Int, Int>,
        navController: NavController,
        viewGroup: ViewGroup
    ) {
        // 1. 为每个视图设置点击监听器
        for ((viewId, destinationId) in viewsToDestinations) {
            val view = viewGroup.findViewById<View>(viewId)
            view?.setOnClickListener {
                // 调用我们自定义的导航方法
                navigateToDestination(navController, destinationId)
            }
        }

        // 2. 添加一个 OnDestinationChangedListener 来更新按钮的“选中”状态
        val weakReference = WeakReference(viewGroup)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }

                    // 遍历所有子视图，更新它们的选中状态
                    viewGroup.forEach { childView ->
                        // 查找哪个按钮对应于当前的目的地
                        val destinationId = viewsToDestinations[childView.id]

                        // 更新视图的视觉状态（例如，改变背景、颜色等）
                        childView.isSelected = (destinationId == destination.id)
                    }
                }
            })
    }

    /**
     * 【核心】一个模仿 NavigationUI.onNavDestinationSelected 的导航方法。
     * 它构建了正确的 NavOptions 来实现 show/hide 行为。
     */
     fun navigateToDestination(navController: NavController, @IdRes destId: Int) {
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true) // 恢复状态
            .setPopUpTo(
                navController.graph.startDestinationId,
                inclusive = false,
                saveState = true // 保存状态
            )
        val options = builder.build()
        try {
            // 执行导航
            navController.navigate(destId, null, options)
        } catch (e: IllegalArgumentException) {
            // 捕获当目标 destination 找不到时的异常
            e.printStackTrace()
        }
    }
}