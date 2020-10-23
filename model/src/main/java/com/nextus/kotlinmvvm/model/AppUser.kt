package com.nextus.kotlinmvvm.model

import androidx.annotation.Keep

@Keep
data class AppUser(
    val uuid: String,
    val nickname: String,
    val address: String? = "",
    val email: String? = "",
    val profile_url: String? = ""
)