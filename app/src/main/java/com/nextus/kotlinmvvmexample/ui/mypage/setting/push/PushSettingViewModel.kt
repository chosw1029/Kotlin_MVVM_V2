package com.nextus.kotlinmvvmexample.ui.mypage.setting.push

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextus.kotlinmvvmexample.shared.domain.prefs.NotificationsPrefSaveActionUseCase
import com.nextus.kotlinmvvmexample.shared.domain.settings.GetAnalyticsSettingUseCase
import com.nextus.kotlinmvvmexample.shared.domain.settings.GetNotificationsSettingUseCase
import com.nextus.kotlinmvvmexample.shared.result.data
import com.nextus.kotlinmvvmexample.shared.result.updateOnSuccess
import kotlinx.coroutines.launch

class PushSettingViewModel @ViewModelInject constructor(
    getAnalyticsSettingUseCase: GetAnalyticsSettingUseCase,
    getNotificationsSettingUseCase: GetNotificationsSettingUseCase,
    val notificationsPrefSaveActionUseCase: NotificationsPrefSaveActionUseCase,
): ViewModel() {

    // Notifications setting
    private val _enableNotifications = MutableLiveData<Boolean>()
    val enableNotifications: LiveData<Boolean>
        get() = _enableNotifications

    init {
        // Executing use cases in parallel
        viewModelScope.launch {
            _enableNotifications.value = getNotificationsSettingUseCase(Unit).data ?: false
        }
    }

    fun toggleEnableNotifications(checked: Boolean) {
        viewModelScope.launch {
            notificationsPrefSaveActionUseCase(checked).updateOnSuccess(_enableNotifications)
        }
    }

}