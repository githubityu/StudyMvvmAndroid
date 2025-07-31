package com.ityu.studymvvmandroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ityu.studymvvmandroid.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String
)
fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email
    )
}