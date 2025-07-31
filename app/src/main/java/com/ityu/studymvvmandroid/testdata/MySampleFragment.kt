package com.ityu.studymvvmandroid.testdata

import android.content.Context
import android.view.View
import com.ityu.studymvvmandroid.testdata.TestFragment

// 具体的 Fragment 实现
class MySampleFragment : TestFragment() {
    init {
        println("MySampleFragment: Instance created (constructor).")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("MySampleFragment: onAttach called.")
    }

    override fun onCreate() {
        super.onCreate()
        println("MySampleFragment: onCreate called.")
    }

    override fun onCreateView(): View {
        println("MySampleFragment: onCreateView called.")
        // 在真实场景中，这里会 inflate 一个 XML 布局
        return View(requireContext()) // 简单返回一个 View 对象
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        println("MySampleFragment: onViewCreated called.")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("MySampleFragment: onDestroyView called.")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MySampleFragment: onDestroy called.")
    }

    override fun onDetach() {
        super.onDetach()
        println("MySampleFragment: onDetach called.")
    }
}

// 主程序入口
fun main() {
    var fakeContext: Context? = null // 使用 Mockito 或手动创建一个假的 Context
    val manager = FragmentContainer()

    println("--- Adding Fragment ---")
    val fragment = MySampleFragment()
    if (fakeContext != null) {
        manager.addFragment(fragment, fakeContext)
    }
    println("\n--- Fragment is running ---\n")

    println("--- Removing Fragment ---")
    manager.removeCurrentFragment()
}