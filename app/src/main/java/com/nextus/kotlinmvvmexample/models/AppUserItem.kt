package com.nextus.kotlinmvvmexample.models

import android.os.Parcelable
import com.nextus.kotlinmvvm.model.AppUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppUserItem(
    val _id: String,
    val uid: String,
    val nickname: String,
    val imageUrl: String,
    val updatedAt: String,
    val createdAt: String
): Parcelable {
    fun getProfileImage() = "$uid/$imageUrl"
}

fun AppUser.mapToPresentation() = AppUserItem(
    _id, uid, nickname, imageUrl, updatedAt, createdAt
)