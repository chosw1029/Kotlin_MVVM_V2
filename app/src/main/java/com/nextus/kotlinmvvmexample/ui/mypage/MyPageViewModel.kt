package com.nextus.kotlinmvvmexample.ui.mypage

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.nextus.kotlinmvvmexample.ui.main.MainFragmentDirections.Companion.toEditProfile
import com.nextus.kotlinmvvmexample.ui.main.MainFragmentDirections.Companion.toSetting
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate

class MyPageViewModel @ViewModelInject constructor(
    private val signInViewModelDelegate: SignInViewModelDelegate
): ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    fun onClickProfile(view: View) {
        view.findNavController().navigate(toEditProfile())
    }

    fun onClickSetting(view: View) {
        view.findNavController().navigate(toSetting())
    }

}