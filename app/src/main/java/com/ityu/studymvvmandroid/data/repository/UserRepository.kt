package com.ityu.studymvvmandroid.data.repository

import com.ityu.studymvvmandroid.data.remote.ApiService
import com.ityu.studymvvmandroid.data.local.UserDao
import com.ityu.studymvvmandroid.data.local.toDomain
import com.ityu.studymvvmandroid.domain.model.User
import com.ityu.studymvvmandroid.domain.model.generateFakeUsers
import com.ityu.studymvvmandroid.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// Repository
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    suspend fun getUsers(page: Int = 1, pageSize: Int = 10): Flow<Result<List<User>>> = flow {
        try {
            // First, emit cached data
            val cachedUsers = userDao.getAllUsers().map { it.toDomain() }
            emit(Result.success(cachedUsers))

            // Then fetch from network
//            val networkUsers = apiService.getUsers()
            val networkUsers = generateFakeUsers(20)
            userDao.insertUsers(networkUsers.map { it.toEntity() })
            emit(Result.success(networkUsers))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}