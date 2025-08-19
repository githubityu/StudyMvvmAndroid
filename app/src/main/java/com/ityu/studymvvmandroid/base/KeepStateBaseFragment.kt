package com.ityu.studymvvmandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ityu.studymvvmandroid.utils.LogUtils

/**
 * 一个高度优化的、基于“手动缓存视图”模式的 BaseFragment。
 *
 * 结合了：
 * - 视图缓存技术，用于快速恢复UI状态。
 * - 构造函数注入 Binding Inflater，简化子类实现。
 * - 生命周期安全的方法访问 Binding，避免崩溃。
 *
 * @param bindingInflater 用于创建 ViewBinding 实例的方法引用，例如：`FragmentMyBinding::inflate`。
 */
abstract class KeepStateBaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    // 优化 1: 属性变为私有，外部通过安全方法访问
    private var _cachedBinding: VB? = null

    // 用于确保初始化逻辑只执行一次的标志位
    private var isViewInitialized = false

    /**
     * 安全地访问 ViewBinding 实例。
     * 只有在 Fragment 视图实际存在时，提供的代码块才会被执行。
     * 这是访问视图元素最安全的方式。
     *
     * @param block 一个以 ViewBinding 实例为参数的代码块。
     */
    protected fun withBinding(block: (binding: VB) -> Unit) {
        // 确保 binding 已被创建且 Fragment 的 view 依然存活
        if (_cachedBinding != null && view != null) {
            block(_cachedBinding!!)
        } else {
            LogUtils.w(
                javaClass.simpleName,
                "withBinding was called but the view is not available."
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_cachedBinding == null) {
            // 优化 2: 使用从构造函数传入的 inflater
            _cachedBinding = bindingInflater.invoke(inflater, container, false)
        }
        // 如果视图已存在，必须从其旧的父容器中移除
        (_cachedBinding!!.root.parent as? ViewGroup)?.removeView(_cachedBinding!!.root)
        return _cachedBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isViewInitialized) {
            onViewReady(savedInstanceState)
            isViewInitialized = true
        }
    }

    /**
     * 视图首次创建并准备就绪时调用。
     * 在此方法中进行所有仅需执行一次的初始化操作，如设置监听器、请求初始数据等。
     */
    protected open fun onViewReady(savedInstanceState: Bundle?) {
        // 子类可以在这里进行初始化
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtils.d(
            javaClass.simpleName,
            "onDestroyView called, view cache is intentionally preserved."
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _cachedBinding = null
        LogUtils.d(javaClass.simpleName, "onDestroy called, clearing view cache finally.")
    }
}