package com.ityu.studymvvmandroid.data.remote

import com.ityu.studymvvmandroid.utils.ApiLoadingState
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Response

class LoadingInterceptor : Interceptor {

    private val minLoadingDurationMillis: Long = 300 // Minimum time loading indicator is displayed
    private val showLoadingDelayMillis: Long = 100 // Delay before showing the indicator

    override fun intercept(chain: Interceptor.Chain): Response {

        // Launch a coroutine to show the loading indicator after a delay
        val showLoadingJob = CoroutineScope(Dispatchers.Main.immediate).launch { // Use Main.immediate to attempt showing instantly
            delay(showLoadingDelayMillis)
            ApiLoadingState.showLoading()
        }

        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val response: Response
        try {
            response = chain.proceed(request)
        } finally {
            val endTime = System.currentTimeMillis()
            val requestDuration = endTime - startTime

            // Ensure the loading indicator is displayed for a minimum duration
            val remainingDelay = maxOf(0, minLoadingDurationMillis - requestDuration)

            // Launch a coroutine to hide the loading indicator after the remaining delay
            CoroutineScope(Dispatchers.Main.immediate).launch {
                delay(remainingDelay)
                showLoadingJob.cancel() //cancel show loading if it's not showing yet
                ApiLoadingState.hideLoading()
            }
        }

        return response
    }
}