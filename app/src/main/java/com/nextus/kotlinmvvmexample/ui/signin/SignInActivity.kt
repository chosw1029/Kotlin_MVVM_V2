package com.nextus.kotlinmvvmexample.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.ActivitySignInBinding
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import com.nextus.kotlinmvvmexample.shared.result.EventObserver
import com.nextus.kotlinmvvmexample.ui.ContainerActivity
import com.nextus.kotlinmvvmexample.ui.signup.SignUpActivity
import com.nextus.kotlinmvvmexample.util.executeAfter
import com.nextus.kotlinmvvmexample.util.signin.SignInHandler
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var signInHandler: SignInHandler

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySignInBinding>(this, R.layout.activity_sign_in)
        binding.executeAfter {
            lifecycleOwner = this@SignInActivity
            viewModel = signInViewModel
        }
        initView()

        subscribeSignInEvent()
    }

    private fun initView() {
        analyticsHelper.sendScreenView(javaClass.simpleName, this)
    }

    private fun subscribeSignInEvent() {
        signInViewModel.performSignInEvent.observe(this, EventObserver { request ->
            if (request == SignInEvent.RequestSignIn) {
                val signInIntent = signInHandler.makeGoogleSignInIntent()
                val observer = object : Observer<Intent?> {
                    override fun onChanged(it: Intent?) {
                        it?.let {
                            startActivityForResult(it, REQUEST_CODE_SIGN_IN)
                            signInIntent.removeObserver(this)
                        }
                    }
                }
                signInIntent.observeForever(observer)
            }
        })
    }

    private fun subscribeAppUser() {
        signInViewModel.currentUserInfo.observe(this, Observer { // 로그인 완료
            it?.let {
                if(it.isSignedIn() && it.getAppUser() == null)
                    goToSignUpActivity()
                else if(it.getAppUser() != null)
                    goToMainActivity()
            }
        })
    }

    private fun goToSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, ContainerActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SIGN_IN) {
            subscribeAppUser()
        }
    }

    companion object {
        const val REQUEST_CODE_SIGN_IN = 42
    }
}