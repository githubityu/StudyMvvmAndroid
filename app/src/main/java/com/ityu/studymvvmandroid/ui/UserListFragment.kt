package com.ityu.studymvvmandroid.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.ityu.studymvvmandroid.base.BaseListFragment
import com.ityu.studymvvmandroid.base.LoadType
import com.ityu.studymvvmandroid.domain.model.UiState
import com.ityu.studymvvmandroid.domain.model.User
import com.ityu.studymvvmandroid.domain.model.UserViewModel
import com.ityu.studymvvmandroid.ui.adapter.UserAdapter
import com.ityu.studymvvmandroid.ui.adapter.UserViewHolder
import com.ityu.studymvvmandroid.utils.PAGE_START
import com.ityu.studymvvmandroid.utils.launchAndRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserListFragment : BaseListFragment<User, UserViewHolder, UserAdapter>() {
    private val viewModel: UserViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    /**
     * Creates the adapter for the visitor list.
     */
    override fun createAdapter(): UserAdapter {
        return UserAdapter(onItemClick = { item ->

        })
    }

    /**
     * Handles user-initiated data loads: Refresh and Load More.
     */
    override fun loadData(loadType: LoadType) {
        // Initial load is handled by the ViewModel, so we ignore it here.
        if (loadType == LoadType.INITIAL) return

        // Prevent duplicate requests or loading more when it's the last page.
        if (isLoading) return
        if (loadType == LoadType.LOAD_MORE && isLastPage) return

        onLoading(loadType)

        // Reset page number only on refresh.
        if (loadType == LoadType.REFRESH) {
            currentPage = PAGE_START
        }

        // Call the ViewModel to fetch the data for the current page.
        viewModel.fetchData(currentPage, loadType)
    }

    /**
     * Observes the ViewModel's state and updates the UI via base class methods.
     */
    private fun observeViewModel() {
        launchAndRepeatOnLifecycle {
            viewModel.users.collectLatest { result ->
                when (result) {
                    is UiState.Success -> onDataLoaded(result.data, determineLoadType())
                    is UiState.Error -> onError(Throwable(result.message), determineLoadType())
                    is UiState.Loading -> onLoading(LoadType.INITIAL) // Let base class show loader if list is empty
                    is UiState.Idle -> { /* Do nothing, waiting for init load */
                    }
                }
            }
        }
    }

    /**
     * Helper to determine the load type based on the current UI state.
     */
    private fun determineLoadType(): LoadType {
        return when {
            binding.swipeRefreshLayout.isRefreshing -> LoadType.REFRESH
            listAdapter.currentList.isEmpty() -> LoadType.INITIAL
            else -> LoadType.LOAD_MORE
        }
    }


}