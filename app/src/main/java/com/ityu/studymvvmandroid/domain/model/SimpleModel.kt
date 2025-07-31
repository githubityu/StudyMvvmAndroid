package com.ityu.studymvvmandroid.domain.model

import com.ityu.studymvvmandroid.data.local.UserEntity
import com.ityu.studymvvmandroid.ui.adapter.Diffable
import io.github.serpro69.kfaker.Faker

// Data Models
data class User(
    val id: Int,
    val name: String,
    val email: String
): Diffable {
    override val diffId: Any = id
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        email = this.email
    )
}
fun generateFakeUser(): User {
    val faker = Faker() // 创建 Faker 实例
    return User(
        id = faker.random.nextInt(1, 100), // 生成 1 到 100 之间的随机整数
        name = faker.name.firstName(), // 生成随机全名
        email = faker.internet.email() // 生成随机电子邮件地址

    )
}
fun generateFakeUsers(count: Int): List<User> {
    return (1..count).map { generateFakeUser() } // 生成指定数量的 User 对象
}
sealed class UiState<out T> {
    data object Idle : UiState<Nothing>() // 初始/空闲状态
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
}
