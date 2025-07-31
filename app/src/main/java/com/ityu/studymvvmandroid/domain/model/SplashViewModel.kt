package com.ityu.studymvvmandroid.domain.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ityu.studymvvmandroid.data.repository.UserRepository
import com.ityu.studymvvmandroid.di.AppStatusManager

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val homeRepository: UserRepository,
    private val appStatusManager: AppStatusManager
) : ViewModel() {

    private val _userState = MutableStateFlow<UiState<List<User>>>(UiState.Idle)

    init {
        signInAndPrepare()
    }

    private fun signInAndPrepare() {
        viewModelScope.launch {
            homeRepository.getUsers()
                .collectLatest { result ->
                    when {
                        result.isSuccess -> {
                            // --- 登录成功后的处理 ---
                            val user = result.getOrNull()
                            if (user != null) {
                                // 1. 保存用户信息
                                _userState.value = UiState.Success(user)
                                // **核心修改：在这里添加1秒的延迟**
                            } else {
                                _userState.value = UiState.Error("Login successful but no user data.")
                            }
                            // 在成功分支的最后，隐藏启动页
                            appStatusManager.setAppReady(true)
                        }

                        result.isFailure -> {
                            // --- 登录失败后的处理 ---
                            _userState.value = UiState.Error(result.exceptionOrNull()?.message)

                            // 登录失败后，我们通常希望立即隐藏启动画面，不进行额外延迟
                            appStatusManager.setAppReady(true)
                        }
                    }
                }
        }
    }
}