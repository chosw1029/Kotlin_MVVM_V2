package com.nextus.kotlinmvvmexample.ui.mypage

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.navigation.findNavController
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.ui.main.MainFragmentDirections.Companion.toEditProfile
import com.nextus.kotlinmvvmexample.ui.main.MainFragmentDirections.Companion.toSetting
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate

class MyPageViewModel @ViewModelInject constructor(
    private val signInViewModelDelegate: SignInViewModelDelegate
): ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _navigateToSignInDialogAction = MutableLiveData<Event<Unit>>()
    val navigateToSignInDialogAction: LiveData<Event<Unit>>
        get() = _navigateToSignInDialogAction

    val nickname = currentAppUserInfo.map { it?.nickname ?: "로그인하기" }

    fun onClickProfile(view: View) {
        if(isSignedIn()) {
            view.findNavController().navigate(toEditProfile())
        } else
            _navigateToSignInDialogAction.value = Event(Unit)
    }

    fun onClickSetting(view: View) {
        if(isSignedIn())
            view.findNavController().navigate(toSetting())
        else
            _navigateToSignInDialogAction.value = Event(Unit)
    }

}