package com.nextus.kotlinmvvmexample.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import com.nextus.kotlinmvvmexample.ui.base.NavigationHost
import com.nextus.kotlinmvvmexample.ui.message.SnackbarMessageManager
import com.nextus.kotlinmvvmexample.util.PERMISSION_REQUEST
import com.nextus.kotlinmvvmexample.util.PermissionUtils
import com.nextus.kotlinmvvmexample.util.shouldShowRequestPermissionRationaleCompat
import com.nextus.kotlinmvvmexample.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_container.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ContainerActivity : AppCompatActivity(), NavigationHost {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        PermissionUtils.initVariables(this, containerViewModel, null, content_container)
        MobileAds.initialize(this) {}
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar, title: String?) {
        setSupportActionBar(toolbar)
        toolbar.apply {
            setupWithNavController(navController)
            title?.let { setTitle(title) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST) {
            var permissionResult = true

            for(result in grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED) {
                    permissionResult = false
                    break
                }
            }

            if(permissionResult) {
                containerViewModel.permissionGranted()
            } else {
                if(shouldShowRequestPermissionRationaleCompat(requiredPermissions[0]) ||
                        shouldShowRequestPermissionRationaleCompat(requiredPermissions[1])) {
                    findViewById<ConstraintLayout>(R.id.content_container).showSnackbar(R.string.permission_denied, Snackbar.LENGTH_INDEFINITE, R.string.ok) {}
                } else { // 다시 보지 않음을 체크하고 거부한 경우
                    findViewById<ConstraintLayout>(R.id.content_container).showSnackbar(R.string.permission_denied_permanent, Snackbar.LENGTH_INDEFINITE, R.string.ok) {}
                }
            }
        }
    }
}
