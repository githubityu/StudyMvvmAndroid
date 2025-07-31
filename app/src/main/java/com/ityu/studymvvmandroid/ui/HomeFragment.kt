package com.ityu.studymvvmandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ityu.studymvvmandroid.R

import com.ityu.studymvvmandroid.base.BaseFragment
import com.ityu.studymvvmandroid.databinding.FragmentHomeBinding
import com.ityu.studymvvmandroid.ui.adapter.SimpleAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView.text = "HomeFragment"
        val data = (1..100).map { "Item $it" }
        val adapter = SimpleAdapter({ item ->
            requireActivity().findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.UserListFragment)

            findNavController()
        }, data)
        binding.recyclerViewState.adapter = adapter
    }
}