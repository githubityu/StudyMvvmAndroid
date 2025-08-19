package com.ityu.studymvvmandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ityu.studymvvmandroid.utils.LogUtils


abstract class BaseFragment<VB : ViewBinding>(private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    Fragment() {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        LogUtils.d(javaClass.simpleName, "onHiddenChanged=>$hidden")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Important to prevent memory leaks
        //打印日志
        LogUtils.d(javaClass.simpleName, "onDestroyView")
    }

}