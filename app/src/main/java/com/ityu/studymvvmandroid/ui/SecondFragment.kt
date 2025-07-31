package com.ityu.studymvvmandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ityu.studymvvmandroid.base.BaseFragment
import com.ityu.studymvvmandroid.databinding.FragmentSecondBinding
import com.ityu.studymvvmandroid.domain.model.SharedViewModel
import com.ityu.studymvvmandroid.utils.ApiLoadingState


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : BaseFragment<FragmentSecondBinding>(FragmentSecondBinding::inflate) {


    private val sharedViewModel: SharedViewModel by activityViewModels() // Use activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
           val action =  SecondFragmentDirections.actionSecondFragmentToTabFragment()
            findNavController().navigate(action)
        }
        binding.buttonSecond.setOnClickListener {
            sharedViewModel.updateSharedData("update data by 2")
            ApiLoadingState.showLoading()
        }
        binding.buttonThird.setOnClickListener {
            sharedViewModel.updateSharedData("update data by 2")
            ApiLoadingState.hideLoading()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
}