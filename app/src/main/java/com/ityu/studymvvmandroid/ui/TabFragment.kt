package com.ityu.studymvvmandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ityu.studymvvmandroid.R
import com.ityu.studymvvmandroid.base.BaseFragment
import com.ityu.studymvvmandroid.databinding.FragmentTabBinding

class TabFragment : BaseFragment<FragmentTabBinding>(FragmentTabBinding::inflate) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab, container, false)

        // 找到嵌套的 NavHostFragment
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nested_nav_host_fragment) as NavHostFragment

        // 从嵌套的 NavHostFragment 中获取 NavController
        val nestedNavController = navHostFragment.navController

        // 找到 BottomNavigationView
        val bottomNavView = view.findViewById<BottomNavigationView>(R.id.container_bottom_nav)

        // 将 BottomNavigationView 与嵌套的 NavController 连接
        bottomNavView.setupWithNavController(nestedNavController)
        return view
    }

}