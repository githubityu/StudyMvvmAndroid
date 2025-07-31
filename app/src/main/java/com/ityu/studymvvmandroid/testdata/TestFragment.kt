package com.ityu.studymvvmandroid.testdata

import android.content.Context
import android.view.View

abstract class TestFragment {

    private var context: Context? = null // 模拟与宿主的关联
    private var view: View? = null      // 模拟视图的持有（我帮你加上了这句）

    // 1. 挂载阶段
    open fun onAttach(context: Context) {
        this.context = context
    }
    open fun requireContext(): Context {
        return context ?: throw IllegalStateException("Fragment is not attached to a context.")
    }

    // 2. 实例创建阶段
    open fun onCreate() {
        // 实例相关的初始化
    }

    // 3. 视图创建阶段 (核心)
    abstract fun onCreateView(): View // 强制子类实现UI的创建

    // 4. 视图创建完毕阶段
    open fun onViewCreated(view: View) {
        // 对刚刚创建的view进行操作
        this.view = view // 持有视图的引用
    }

    // 5. 视图销毁阶段
    open fun onDestroyView() {
        this.view = null // 解除对视图的引用，防止内存泄漏
    }

    // 6. 实例销毁阶段
    open fun onDestroy() {
        //将实例删除 (在外部的FragmentManager中完成)
    }

    // 7. 解除挂载阶段
    open fun onDetach() {
        context = null // 解除对Context的引用，防止内存泄漏
    }
}