package com.ityu.studymvvmandroid.data.remote

import com.ityu.studymvvmandroid.domain.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}