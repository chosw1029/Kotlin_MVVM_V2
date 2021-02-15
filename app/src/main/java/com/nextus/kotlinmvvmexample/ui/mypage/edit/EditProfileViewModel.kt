package com.nextus.kotlinmvvmexample.ui.mypage.edit

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate
import com.nextus.kotlinmvvmexample.util.PermissionUtils

class EditProfileViewModel @ViewModelInject constructor (
    private val signInViewModelDelegate: SignInViewModelDelegate
): ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    val removeImageEvent = MutableLiveData<Event<Unit>>()
    val removeIconVisibility = MutableLiveData(false)

    val nickname = MutableLiveData(getNickname().value ?: "")

    fun onClickProfileImage() {
        PermissionUtils.checkPermission()
    }

    fun onClickRemoveImage() {
        removeIconVisibility.value = false
        removeImageEvent.value = Event(Unit)
    }
}