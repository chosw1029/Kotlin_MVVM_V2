package com.nextus.kotlinmvvmexample.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate
import timber.log.Timber

class ContainerViewModel  @ViewModelInject constructor(
    private val signInViewModelDelegate: SignInViewModelDelegate,
) : ViewModel(), SignInViewModelDelegate by signInViewModelDelegate {

    private val _grantedEvent = MutableLiveData<Event<Unit>>()
    val grantedEvent: LiveData<Event<Unit>> = _grantedEvent

    fun permissionGranted() {
        _grantedEvent.value = Event(Unit)
    }

}