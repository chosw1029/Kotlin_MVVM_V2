package com.nextus.kotlinmvvmexample.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.ads.MobileAds
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import com.nextus.kotlinmvvmexample.ui.base.NavigationHost
import com.nextus.kotlinmvvmexample.ui.message.SnackbarMessageManager
import com.nextus.kotlinmvvmexample.util.PermissionUtils
import com.nextus.kotlinmvvmexample.util.signin.FirebaseAuthErrorCodeConverter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_container.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ContainerActivity : AppCompatActivity(), NavigationHost {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    private val containerViewModel: ContainerViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        initView()
    }

    private fun initView() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        PermissionUtils.initVariables(this, containerViewModel, content_container)
        MobileAds.initialize(this) {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            Timber.d("An activity returned RESULT_CANCELED")
            val response = IdpResponse.fromResultIntent(data)
            response?.error?.let {
                snackbarMessageManager.addMessage(
                    SnackbarMessage(
                        messageId = FirebaseAuthErrorCodeConverter.convert(it.errorCode),
                        requestChangeId = UUID.randomUUID().toString()
                    )
                )
            }
        }
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar, title: String?) {
        setSupportActionBar(toolbar)
        toolbar.apply {
            setupWithNavController(navController)
            title?.let { setTitle(title) }
        }
    }
}
