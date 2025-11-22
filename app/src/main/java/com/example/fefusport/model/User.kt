package com.example.fefusport.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["login"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val login: String,
    val password: String,
    val nickname: String,
    val gender: String,
    val age: Int,
    val avatarUri: String? = null
)

