package com.ityu.studymvvmandroid.testdata
import android.content.Context

// 假设这是我们模拟的 Fragment 容器
class FragmentContainer {
    private var currentFragment: TestFragment? = null

    // 模拟 transaction.add()
    fun addFragment(fragment: TestFragment, context: Context) {
        // 如果有旧的 Fragment，先销毁它 (简化版的 replace)
        currentFragment?.let { detachAndDestroy(it) }

        // --- 开始驱动新 Fragment 的生命周期 ---
        fragment.onAttach(context)
        fragment.onCreate()
        val view = fragment.onCreateView()
        fragment.onViewCreated(view)
        // ... 可以在这里添加 onStart, onResume 的调用 ...

        this.currentFragment = fragment
        println("${fragment.javaClass.simpleName} has been added and is active.")
    }

    // 模拟 transaction.remove() 或者 Activity 销毁
    fun removeCurrentFragment() {
        currentFragment?.let {
            detachAndDestroy(it)
            currentFragment = null
            println("Fragment has been removed.")
        }
    }

    // 将销毁逻辑封装起来
    private fun detachAndDestroy(fragment: TestFragment) {
        // --- 开始驱动 Fragment 的销毁生命周期 ---
        // ... onPause, onStop ...
        fragment.onDestroyView()
        fragment.onDestroy()
        fragment.onDetach()
    }
}