package com.nextus.kotlinmvvmexample.ui.mypage.setting.push

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentPushSettingBinding
import com.nextus.kotlinmvvmexample.databinding.FragmentSettingBinding
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PushSettingFragment : BaseFragment<FragmentPushSettingBinding>(R.layout.fragment_push_setting) {

    private val pushSettingViewModel: PushSettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(pushSettingViewModel)
        initView()
    }

    private fun initView() {

    }
    
    override fun onResume() {
        super.onResume()
        navigationHost?.registerToolbarWithNavigation(getBinding().toolbar, getString(R.string.title_push_setting))
    }
}