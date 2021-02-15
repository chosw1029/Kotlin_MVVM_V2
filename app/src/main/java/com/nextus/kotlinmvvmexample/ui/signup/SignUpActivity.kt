package com.nextus.kotlinmvvmexample.ui.signup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.ActivitySignUpBinding
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import com.nextus.kotlinmvvmexample.shared.result.EventObserver
import com.nextus.kotlinmvvmexample.ui.ContainerActivity
import com.nextus.kotlinmvvmexample.util.*
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_container.*
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val signUpViewModel: SignUpViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.executeAfter {
            lifecycleOwner = this@SignUpActivity
            viewModel = signUpViewModel
        }
        initView()

        subscribePermissionEvent()
        subscribeNicknameInputLayout()
        subscribeRemoveImageEvent()
        subscribeSignUpEvent()
        subscribeErrorEvent()
    }

    private fun initView() {
        PermissionUtils.initVariables(this, null, signUpViewModel, binding.root)
        analyticsHelper.sendScreenView(javaClass.simpleName, this)
    }

    private fun subscribeNicknameInputLayout() {
        binding.nicknameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if(s.toString().contains(" ")) {
                    binding.nicknameEditText.setText(s.toString().replace(" ", ""))
                    binding.nicknameEditText.setSelection(binding.nicknameEditText.text.toString().length)
                }

                signUpViewModel.isShowError.value = s.toString().contentEquals(signUpViewModel.submitNickname.value!!)
            }
        })
    }

    private fun subscribeRemoveImageEvent() {
        signUpViewModel.removeImageEvent.observe(this, EventObserver {
            Glide.with(this)
                    .load(AppCompatResources.getDrawable(this, R.drawable.ic_hero))
                    .into(binding.profileImage)
        })
    }

    private fun subscribePermissionEvent() {
        signUpViewModel.grantedEvent.observe(this, EventObserver {
            TedImagePicker.with(this)
                    .min(1, "")
                    .max(5, "You can only select 5 images.")
                    .startMultiImage { uriList ->
                        uriList.map { uri ->
                            contentResolver.query(uri, null, null, null, null )?.use { cursor ->
                                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                                while(cursor.moveToNext()) {
                                    val id = cursor.getLong(idColumn)
                                    val size = cursor.getString( cursor.getColumnIndex( OpenableColumns.SIZE ))
                                    val sourceFileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ))

                                    val file = File(filesDir, sourceFileName)

                                    if(sourceFileName.endsWith(".gif")) {
                                        if(BitmapUtils.getSizeToMega(size ?: "0") > 10) {
                                            Toast.makeText(this, "Image size is too big (less than 10MB)", Toast.LENGTH_SHORT).show()
                                        } else {
                                            contentResolver.openInputStream(uri).use { stream ->
                                                stream?.let { inputStream ->
                                                    BitmapUtils.copyStreamToFile(inputStream, file)
                                                    Glide.with(this).load(file)
                                                            .placeholder(R.drawable.ic_hero)
                                                            .into(binding.profileImage)
                                                }
                                            }
                                        }
                                    } else {
                                        contentResolver.openFileDescriptor(uri, "r").use { pfd ->
                                            val bitmap = BitmapFactory.decodeFileDescriptor(pfd?.fileDescriptor)
                                            Glide.with(this).load(BitmapUtils.resizeAndCompressImage(bitmap, file, size ?: "0"))
                                                    .placeholder(R.drawable.ic_hero)
                                                    .into(binding.profileImage)
                                        }
                                    }
                                }
                            }
                        }
                    }
        })
    }

    private fun subscribeSignUpEvent() {
        signUpViewModel.signUpSuccessEvent.observe(this, EventObserver {
            startActivity(Intent(this, ContainerActivity::class.java))
            finish()
        })
    }

    private fun subscribeErrorEvent() {
        signUpViewModel.isShowError.observe(this, Observer {
            binding.nicknameInputLayout.error = "That name isn't available."
        })
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
                signUpViewModel.permissionGranted()
            } else {
                if(shouldShowRequestPermissionRationaleCompat(requiredPermissions[0]) ||
                        shouldShowRequestPermissionRationaleCompat(requiredPermissions[1])) {
                    binding.contentContainer.showSnackbar(R.string.permission_denied, Snackbar.LENGTH_INDEFINITE, R.string.ok) {}
                } else { // 다시 보지 않음을 체크하고 거부한 경우
                    binding.contentContainer.showSnackbar(R.string.permission_denied_permanent, Snackbar.LENGTH_INDEFINITE, R.string.ok) {}
                }
            }
        }
    }
}