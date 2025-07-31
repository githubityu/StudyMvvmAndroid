package com.ityu.studymvvmandroid.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ityu.studymvvmandroid.utils.LogUtils

/**
 * 一个基于“手动缓存视图”模式的 BaseFragment，其原理源自 StackOverflow 中 Ian Lake 的建议。
 *
 * 工作原理:
 * 1. 在 `onCreateView` 中，如果视图是第一次创建，则正常加载并将其引用缓存起来。
 * 2. 如果视图已被缓存，则直接返回缓存的实例，但在此之前必须将其从旧的父视图中移除。
 * 3. `onDestroyView` 会被正常调用，但我们故意不在此方法中清除视图的缓存，以实现状态保持。
 * 4. 使用一个标志位 `isViewInitialized` 来确保昂贵的初始化操作（如设置监听器）只执行一次。
 *
 * **注意：这是一种与自定义 Navigator 的 show/hide 机制不同的、更手动的实现方式。**
 */
abstract class KeepStateBaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _cachedBinding: VB? = null
    protected val binding: VB
        get() = _cachedBinding!!


    // 用于确保初始化逻辑只执行一次的标志位
    private var isViewInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 检查是否已经有缓存的 binding
        if (_cachedBinding != null) {
            // 关键！如果视图已经有一个父视图，必须先将它移除，否则会抛出 IllegalStateException
            (_cachedBinding!!.root.parent as? ViewGroup)?.removeView(_cachedBinding!!.root)
        } else {
            // 第一次创建，加载视图并缓存 binding
            _cachedBinding = bindingInflater.invoke(inflater, container, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 此方法每次 onCreateView 返回有效 View 后都会被调用。
        // 我们使用标志位来防止重复执行初始化代码。
        if (!isViewInitialized) {
            onViewReady(savedInstanceState)
            isViewInitialized = true
        } else {
            LogUtils.d(javaClass.simpleName, "onViewCreated: View already initialized, skipping.")
        }
    }

    /**
     * 视图已准备好，可以在此方法中进行所有仅需执行一次的初始化操作。
     * 子类如果重写此方法，强烈建议在方法开始处调用 super.onViewReady(savedInstanceState)
     * 以保留基类的功能（如全局加载状态监听）。
     *
     * @param savedInstanceState 如果 Fragment 是从一个之前保存的状态中重新创建的，则为非 null。
     */
    protected open fun onViewReady(savedInstanceState: Bundle?) {
        LogUtils.d(javaClass.simpleName, "Base onViewReady: Setting up global observers.")
        // --- END: 新增部分 ---
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // 这是此模式与标准模式最大的不同之处：
        // 我们故意不在这里将 _cachedBinding 置为 null。
        // 这允许我们在下次调用 onCreateView 时能够返回缓存的视图。
        // 这也是为什么 LeakCanary 可能会误报内存泄漏的原因。
        LogUtils.d(
            javaClass.simpleName,
            "onDestroyView called, but view cache is intentionally preserved."
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        // 当 Fragment 实例本身被销毁时（例如，从返回栈中彻底移除），
        // 我们才最终清理缓存，以防止真正的内存泄漏。
        _cachedBinding = null
        LogUtils.d(javaClass.simpleName, "onDestroy called, clearing view cache finally.")
    }
}