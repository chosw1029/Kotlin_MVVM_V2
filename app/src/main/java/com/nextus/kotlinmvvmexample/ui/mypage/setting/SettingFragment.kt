package com.nextus.kotlinmvvmexample.ui.mypage.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentSettingBinding
import com.nextus.kotlinmvvmexample.shared.result.EventObserver
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment: BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(settingViewModel)
        initView()
    }

    private fun initView() {
        subscribeSignOutEvent()
    }

    private fun subscribeSignOutEvent() {
        settingViewModel.signOutEvent.observe(viewLifecycleOwner, EventObserver {
            showSignOutDialog()
        })
    }

    private fun showSignOutDialog() {
        MaterialAlertDialogBuilder(context)
                .setTitle(R.string.sign_out)
                .setMessage(R.string.dialog_sign_out_content)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok) { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigateUp()
                }
                .create()
                .show()
    }
    
    override fun onResume() {
        super.onResume()
        navigationHost?.registerToolbarWithNavigation(getBinding().toolbar, getString(R.string.title_setting))
    }
}