package com.nextus.kotlinmvvm.model

import androidx.annotation.Keep

data class AppUser(
    val _id: String,
    val uid: String,
    val nickname: String,
    val imageUrl: String? = "",
    val updatedAt: String,
    val createdAt: String
)