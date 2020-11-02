package com.nextus.kotlinmvvmexample.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate

class ContainerViewModel  @ViewModelInject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val signInViewModelDelegate: SignInViewModelDelegate,
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _grantedEvent = MutableLiveData<Event<String>>()
    val grantedEvent: LiveData<Event<String>> = _grantedEvent

    fun permissionGranted(fragmentName: String) {
        _grantedEvent.value = Event(fragmentName)
    }

}