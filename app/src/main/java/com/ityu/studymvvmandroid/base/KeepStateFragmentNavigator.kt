package com.ityu.studymvvmandroid.base
// KeepStateFragmentNavigator.kt (New and improved version)
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.ityu.studymvvmandroid.utils.LogUtils

@Navigator.Name("fragment")
class KeepStateFragmentNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    /**
     * 重写这个核心的导航方法。
     * @param entries NavController 回退栈中的所有条目，最后一个是当前目标。
     * @param navOptions 导航动画等选项。
     * @param navigatorExtras 共享元素转换等额外信息。
     */
    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        LogUtils.i("KeepStateFragmentNavigator", "entries: $entries, navOptions: $navOptions, navigatorExtras: $navigatorExtras")
        // 如果 FragmentManager 正在保存状态，则不执行任何操作。
        // 这是为了防止在 Activity 因配置更改等原因重建时出现状态不一致。
        if (manager.isStateSaved) {
            // Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state")
            return
        }

        // 1. 开始 Fragment 事务
        val transaction = manager.beginTransaction()

        // 2. 获取当前显示的 Fragment (准备隐藏它)
        val currentFragment = manager.primaryNavigationFragment

        // 3. 获取目标 Fragment (准备显示它)
        // 目标 destination 总是列表中的最后一个
        val destinationEntry = entries.last()
        val destination = destinationEntry.destination as Destination // 强转为 FragmentNavigator.Destination
        val tag = destination.id.toString()

        // 4. 处理隐藏和显示逻辑
        if (currentFragment != null) {
            // 如果当前有 Fragment 显示，则隐藏它
            transaction.hide(currentFragment)
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            // 目标 Fragment 不存在：创建新实例并添加
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            // 将参数传递给 Fragment
            fragment.arguments = destinationEntry.arguments
            transaction.add(containerId, fragment, tag)
        } else {
            // 目标 Fragment 已存在：直接显示
            // 同样需要更新参数，以防导航时携带了新数据
            fragment.arguments = destinationEntry.arguments
            transaction.show(fragment)
        }

        // 5. 设置主导航 Fragment
        // 这一步告诉 FragmentManager 哪个 Fragment 是当前活动的，对于分发返回事件等至关重要
        transaction.setPrimaryNavigationFragment(fragment)

        // 6. 设置事务的 reordering 和 commit
        transaction.setReorderingAllowed(true)
        transaction.commit()
    }
}