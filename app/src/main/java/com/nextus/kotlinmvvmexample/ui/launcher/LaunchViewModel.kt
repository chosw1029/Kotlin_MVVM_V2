package com.nextus.kotlinmvvmexample.ui.launcher

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate
import kotlinx.coroutines.delay

class LaunchViewModel @ViewModelInject constructor(
    private val signInViewModelDelegate: SignInViewModelDelegate
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

}