package com.ityu.studymvvmandroid.domain.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ityu.studymvvmandroid.base.LoadType
import com.ityu.studymvvmandroid.data.repository.UserRepository
import com.ityu.studymvvmandroid.utils.ApiLoadingState
import com.ityu.studymvvmandroid.utils.LogUtils
import com.ityu.studymvvmandroid.utils.PAGE_START
import com.ityu.studymvvmandroid.utils.collectAsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<UiState<List<User>>>(UiState.Idle)
    val users: StateFlow<UiState<List<User>>> = _users.asStateFlow()


    init {
        // 2. ViewModel 创建时自动加载第一页
        loadData(PAGE_START, LoadType.INITIAL)
    }


    private fun loadData(page: Int, loadType: LoadType) {
        LogUtils.i("loadData", "page: $page, loadType: $loadType")
        viewModelScope.launch {
            // 防止正在加载时重复请求
            if (_users.value is UiState.Loading) return@launch
            if (loadType == LoadType.LOAD_MORE) ApiLoadingState.showLoading()
            userRepository.getUsers(page).collectAsUiState(_users)
            if (loadType == LoadType.LOAD_MORE) ApiLoadingState.hideLoading()
            LogUtils.i("loadData", "page: $page, loadType: $loadType")
        }
    }

    fun fetchData(page: Int, loadType: LoadType) {
        loadData(page, loadType)
    }
}