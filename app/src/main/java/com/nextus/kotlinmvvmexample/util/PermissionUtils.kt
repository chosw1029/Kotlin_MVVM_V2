package com.nextus.kotlinmvvmexample.util

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.ui.ContainerViewModel
import com.nextus.kotlinmvvmexample.ui.signup.SignUpViewModel
import timber.log.Timber

const val PERMISSION_REQUEST = 0

object PermissionUtils {

    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var appCompatActivity: AppCompatActivity? = null
    private var containerViewModel: ContainerViewModel? = null
    private var signUpViewModel: SignUpViewModel? = null
    private var root: View? = null

    fun initVariables(appCompatActivity: AppCompatActivity, containerViewModel: ContainerViewModel?, signUpViewModel: SignUpViewModel?, root: View) {
        this.appCompatActivity = appCompatActivity
        this.containerViewModel = containerViewModel
        this.root = root
    }

    fun checkPermission() {
        val cameraPermissionResult = appCompatActivity?.checkSelfPermissionCompat(requiredPermissions[0])
        val storagePermissionResult = appCompatActivity?.checkSelfPermissionCompat(requiredPermissions[1])

        if(cameraPermissionResult == PackageManager.PERMISSION_GRANTED &&
                storagePermissionResult == PackageManager.PERMISSION_GRANTED) {
            containerViewModel?.permissionGranted()
            signUpViewModel?.permissionGranted()
        } else {
            // 퍼미션을 거부한 적이 있는 경우
            appCompatActivity?.let {
                if(it.shouldShowRequestPermissionRationaleCompat(requiredPermissions[0]) ||
                        it.shouldShowRequestPermissionRationaleCompat(requiredPermissions[1])) {
                    root?.showSnackbar(R.string.request_permission,
                            Snackbar.LENGTH_INDEFINITE, R.string.ok) { _ ->
                        it.requestPermissionsCompat(requiredPermissions, PERMISSION_REQUEST)
                    }
                } else {
                    it.requestPermissionsCompat(requiredPermissions, PERMISSION_REQUEST)
                }
            }
        }
    }
}