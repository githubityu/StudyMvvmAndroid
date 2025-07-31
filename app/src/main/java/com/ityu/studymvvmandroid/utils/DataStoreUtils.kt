package com.ityu.studymvvmandroid.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * 一个用于管理应用偏好设置的 DataStore 工具类。
 *
 * 使用方式:
 * 1. 在应用启动时调用 init 方法初始化。
 * 2. 调用这里的静态方法来读写数据。
 *    - 读取: DataStoreUtils.userTokenFlow.collect { ... }
 *    - 写入: DataStoreUtils.saveUserToken("your_token")
 */


/**
 * 从 DataStore 中安全地读取一个偏好项。
 * 内部封装了 I/O 异常处理和默认值提供。
 *
 * @param T The type of the preference value.
 * @param key The Preferences.Key for the value to retrieve.
 * @param defaultValue The default value to return if the key doesn't exist or an error occurs.
 * @return A Flow that emits the preference value.
 */
fun <T> DataStore<Preferences>.getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
    return this.data
        .catch { exception ->
            if (exception is IOException) {
                // On error, emit empty preferences to allow map to provide the default value
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Get the value from preferences, or return the default value if it doesn't exist
            preferences[key] ?: defaultValue
        }
}

/**
 * 从 DataStore 中安全地读取一个可空的偏好项。
 *
 * @param T The type of the preference value.
 * @param key The Preferences.Key for the value to retrieve.
 * @return A Flow that emits the preference value, or null if it doesn't exist.
 */
fun <T> DataStore<Preferences>.getNullablePreference(key: Preferences.Key<T>): Flow<T?> {
    return this.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[key]
        }
}

object DataStoreUtils {
    private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
    private val dataStore: DataStore<Preferences> by lazy {
        AppContextProvider.getContext().appDataStore
    }

    // 集中定义所有的 Preferences Keys
    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_NAME = stringPreferencesKey("user_name")
        // ... 在这里添加更多你需要的 Key ...
    }

    // --- Dark Mode ---
    private val userIdFlow: Flow<Int?>
        get() = dataStore.getPreference(PreferencesKeys.USER_ID, 0)

    fun getUserId(): Int? {
        return runBlocking { userIdFlow.first() }
    }

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun saveUserInfo(userId: Int, token: String) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.USER_ID] = userId
            settings[PreferencesKeys.USER_TOKEN] = token
        }
    }



    // [新增] 添加一个同步获取 Token 的方法，方便在拦截器等地方使用
    fun getUserToken(): String? {
        return runBlocking { userTokenFlow.first() }
    }

    // --- User Token ---
    private val userTokenFlow: Flow<String?>
        get() = dataStore.getNullablePreference(PreferencesKeys.USER_TOKEN)

    suspend fun saveUserToken(token: String) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.USER_TOKEN] = token
        }
    }

    suspend fun clearUserToken() {
        dataStore.edit { settings ->
            settings.remove(PreferencesKeys.USER_TOKEN)
        }
    }

    // --- User Name ---
    val userNameFlow: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_NAME] ?: "Guest" // 提供一个默认值
        }

    suspend fun saveUserName(name: String) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.USER_NAME] = name
        }
    }
}