package com.nextus.kotlinmvvmexample.ui.mypage.setting

import android.content.pm.PackageInfo
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nextus.kotlinmvvmexample.shared.domain.settings.GetAnalyticsSettingUseCase
import com.nextus.kotlinmvvmexample.shared.result.Event
import com.nextus.kotlinmvvmexample.ui.mypage.setting.SettingFragmentDirections.Companion.toPushSetting

class SettingViewModel @ViewModelInject constructor(
    private val packageInfo: PackageInfo
): ViewModel() {

    val appVersion = MutableLiveData("버전 ${packageInfo.versionName}")

    private val _signOutEvent = MutableLiveData<Event<Unit>>()
    val signOutEvent: LiveData<Event<Unit>> = _signOutEvent

    fun onClickPushSetting(view: View) {
        view.findNavController().navigate(toPushSetting())
    }

    fun onClickLogout() {
        _signOutEvent.value = Event(Unit)
    }
}