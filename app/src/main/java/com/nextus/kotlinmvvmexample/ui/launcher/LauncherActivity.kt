package com.nextus.kotlinmvvmexample.ui.launcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.ui.ContainerActivity
import com.nextus.kotlinmvvmexample.ui.signin.SignInActivity
import com.nextus.kotlinmvvmexample.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_launcher.*
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private val launchViewModel: LaunchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        lifecycleScope.launchWhenCreated {
            delay(3000)
            subscribeAppUser()
        }
    }

    private fun subscribeAppUser() {
        launchViewModel.currentUserInfo.observe(this, Observer { // 로그
            it?.let {
                if (it.getAppUser() != null) { // 기존 유저인 경우
                    startActivity(Intent(this@LauncherActivity, ContainerActivity::class.java))
                    finish()
                } else if(!it.isSignedIn()) { // Firebase 로그인이 안되어 있는 경우
                    goToSignInActivity()
                } else { // Firebase 로그인은 되어 있으나 앱 가입이 안되어 있는 경우
                    goToSignUpActivity()
                }
            } ?: goToSignInActivity()
        })
    }

    private fun goToSignInActivity() {
        startActivity(Intent(this@LauncherActivity, SignInActivity::class.java))
        finish()
    }

    private fun goToSignUpActivity() {
        startActivity(Intent(this@LauncherActivity, SignUpActivity::class.java))
        finish()
    }
}