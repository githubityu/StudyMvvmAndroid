package com.ityu.studymvvmandroid.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ityu.studymvvmandroid.domain.model.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * A reusable extension function to collect a Flow of Result into a MutableStateFlow of UiState.
 * This handles the onStart, catch, and collection logic in a standardized way.
 *
 * @param T The type of the successful data.
 * @param stateFlow The MutableStateFlow that will receive the UiState updates.
 */
suspend fun <T> Flow<Result<T>>.collectAsUiState(
    stateFlow: MutableStateFlow<UiState<T>>
) {
    this
        .onStart {
            // Emit Loading state when the flow starts
            stateFlow.value = UiState.Loading
        }
        .catch { e ->
            // Emit Error state if an exception is caught in the flow
            stateFlow.value = UiState.Error(e.message ?: "An unknown error occurred")
        }
        .collect { result ->
            // Collect the Result and map it to Success or Error UiState
            result.fold(
                onSuccess = { data -> stateFlow.value = UiState.Success(data) },
                onFailure = { e ->
                    stateFlow.value = UiState.Error(e.message ?: "An unknown error occurred")
                }
            )
        }
}

/**
 * 一个封装了 `repeatOnLifecycle` 的扩展函数，用于在 Fragment 中安全地启动协程来收集 Flow。
 *
 * 这个函数会自动在 Fragment 的视图生命周期的 `STARTED` 状态下执行代码块，
 * 并在 `STOPPED` 状态下取消它，从而避免内存泄漏和不必要的工作。
 *
 * @param state 协程应该在哪个生命周期状态下执行，默认为 `Lifecycle.State.STARTED`。
 * @param block 要在协程中执行的代码块。
 */
fun Fragment.launchAndRepeatOnLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    // 使用 viewLifecycleOwner.lifecycleScope，它与 Fragment 的视图生命周期绑定
    viewLifecycleOwner.lifecycleScope.launch {
        // 当视图生命周期至少达到 'state' (默认 STARTED) 时执行，并在变为更低状态时取消
        viewLifecycleOwner.repeatOnLifecycle(state) {
            // 在新的协程作用域中执行传入的代码块
            block()
        }
    }
}

fun AppCompatActivity.launchAndRepeatOnLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit
) {
    // Activity 直接使用 lifecycleScope
    lifecycleScope.launch {
        // Activity 直接使用 lifecycle.repeatOnLifecycle
        lifecycle.repeatOnLifecycle(state) {
            // 在新的协程作用域中执行传入的代码块
            block()
        }
    }
}
