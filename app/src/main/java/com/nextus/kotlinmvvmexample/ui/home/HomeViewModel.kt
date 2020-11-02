package com.nextus.kotlinmvvmexample.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.ui.SnackbarMessage

class HomeViewModel : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<SnackbarMessage>>()
    val snackBarMessage: LiveData<Event<SnackbarMessage>>
        get() = _snackbarMessage


}