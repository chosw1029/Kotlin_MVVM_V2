package com.nextus.kotlinmvvmexample.ui.signup

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.nextus.kotlinmvvmexample.shared.domain.user.CreateUserUseCase
import com.nextus.kotlinmvvmexample.shared.domain.user.IsDuplicateNicknameUseCase
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.shared.result.Result
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate
import com.nextus.kotlinmvvmexample.util.PermissionUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SignUpViewModel @ViewModelInject constructor (
    private val createUserUseCase: CreateUserUseCase,
    private val isDuplicateNicknameUseCase: IsDuplicateNicknameUseCase,
    private val signInViewModelDelegate: SignInViewModelDelegate
): ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _grantedEvent = MutableLiveData<Event<Unit>>()
    val grantedEvent: LiveData<Event<Unit>>
        get() = _grantedEvent

    private val _signUpSuccessEvent = MutableLiveData<Event<Unit>>()
    val signUpSuccessEvent: LiveData<Event<Unit>>
        get() = _signUpSuccessEvent

    val removeImageEvent = MutableLiveData<Event<Unit>>()
    val removeIconVisibility = MutableLiveData(false)
    val nickname = MutableLiveData(currentUserInfo.value?.getDisplayName() ?: "")

    val submitNickname = MutableLiveData("")
    val isShowError = MutableLiveData(false)
    val isLoading = MutableLiveData(false)

    private fun createUser() {
        viewModelScope.launch {
            val userInfo = JsonObject().apply {
                addProperty("uid", getUserId())
                addProperty("nickname", nickname.value!!)
            }

            createUserUseCase(userInfo).collect { result ->
                when (result) {
                    is Result.Success -> {
                        updateAppUser(result.data)
                        _signUpSuccessEvent.postValue(Event(Unit))
                    }
                    is Result.Loading -> isLoading.postValue(true)
                    else -> isLoading.postValue(false)
                }
            }
        }
    }

    private fun checkDuplicate() {
        submitNickname.value = nickname.value!!

        viewModelScope.launch {
            isDuplicateNicknameUseCase(nickname.value!!).collect { result ->
                when (result) {
                    is Result.Success -> {
                        isShowError.value = result.data["count"].asInt > 0

                        if(result.data["count"].asInt == 0)
                            createUser()
                    }
                    is Result.Error -> Timber.e(result.exception)
                    else -> { }
                }
            }
        }
    }

    fun permissionGranted() {
        _grantedEvent.value = Event(Unit)
    }

    fun onClickProfileImage() {
        PermissionUtils.checkPermission()
    }

    fun onClickRemoveImage() {
        removeIconVisibility.value = false
        removeImageEvent.value = Event(Unit)
    }

    fun onClickSignUp() {
        checkDuplicate()
    }
}