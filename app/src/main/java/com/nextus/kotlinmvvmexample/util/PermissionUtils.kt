package com.nextus.kotlinmvvmexample.util

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.ui.ContainerViewModel

const val PERMISSION_REQUEST = 0

object PermissionUtils: ActivityCompat.OnRequestPermissionsResultCallback  {

    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var appCompatActivity: AppCompatActivity? = null
    private var containerViewModel: ContainerViewModel? = null
    private var root: View? = null
    private var fragmentName = ""

    fun initVariables(appCompatActivity: AppCompatActivity, containerViewModel: ContainerViewModel, root: View) {
        this.appCompatActivity = appCompatActivity
        this.containerViewModel = containerViewModel
        this.root = root
    }

    fun checkPermission(fragmentName: String) {
        this.fragmentName = fragmentName

        val cameraPermissionResult = appCompatActivity?.checkSelfPermissionCompat(requiredPermissions[0])
        val storagePermissionResult = appCompatActivity?.checkSelfPermissionCompat(requiredPermissions[1])

        if(cameraPermissionResult == PackageManager.PERMISSION_GRANTED &&
                storagePermissionResult == PackageManager.PERMISSION_GRANTED) {
            containerViewModel?.permissionGranted(fragmentName)
        } else {
            // 퍼미션을 거부한 적이 있는 경우
            appCompatActivity?.let {
                if(it.shouldShowRequestPermissionRationaleCompat(requiredPermissions[0]) ||
                        it.shouldShowRequestPermissionRationaleCompat(requiredPermissions[1])) {
                    root?.showSnackbar(R.string.request_permission,
                            Snackbar.LENGTH_INDEFINITE, R.string.ok) { view ->
                        it.requestPermissionsCompat(requiredPermissions, PERMISSION_REQUEST)
                    }
                } else {
                    it.requestPermissionsCompat(requiredPermissions, PERMISSION_REQUEST)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == PERMISSION_REQUEST) {

            var permissionResult = false

            for(result in grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED) {
                    permissionResult = false
                    break
                }
            }

            if(permissionResult) {
                containerViewModel?.permissionGranted(fragmentName)
            } else {
                appCompatActivity?.let {
                    if(it.shouldShowRequestPermissionRationaleCompat(requiredPermissions[0]) ||
                            it.shouldShowRequestPermissionRationaleCompat(requiredPermissions[1])) {
                        root?.showSnackbar(R.string.permission_denied, Snackbar.LENGTH_INDEFINITE, R.string.ok) {}
                    } else { // 다시 보지 않음을 체크하고 거부한 경우
                        root?.showSnackbar(R.string.permission_denied_permanent, Snackbar.LENGTH_INDEFINITE, R.string.ok) {}
                    }
                }
            }
        }

    }

}