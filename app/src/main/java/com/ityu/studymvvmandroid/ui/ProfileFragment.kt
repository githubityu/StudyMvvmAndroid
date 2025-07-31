package com.ityu.studymvvmandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ityu.studymvvmandroid.R
import com.ityu.studymvvmandroid.base.BaseFragment
import com.ityu.studymvvmandroid.databinding.FragmentHomeBinding
import com.ityu.studymvvmandroid.ui.adapter.SimpleAdapter

class ProfileFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = "ProfileFragment"
        val data = (1..100).map { "Item $it" }
        val adapter = SimpleAdapter({ item ->

        }, data)
        binding.recyclerViewState.adapter = adapter
    }
}