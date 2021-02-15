package com.nextus.kotlinmvvmexample.ui.mypage.edit

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentEditProfileBinding
import com.nextus.kotlinmvvmexample.shared.result.EventObserver
import com.nextus.kotlinmvvmexample.ui.ContainerViewModel
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import com.nextus.kotlinmvvmexample.util.BitmapUtils
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import java.io.File

@AndroidEntryPoint
class EditProfileFragment: BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private val containerViewModel: ContainerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(editProfileViewModel)
        initView()

        subscribePermissionEvent()
        subscribeNicknameInputLayout()
        subscribeRemoveImageEvent()
    }

    private fun initView() {
    }

    private fun subscribeNicknameInputLayout() {
        getBinding().nicknameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if(s.toString().contains(" ")) {
                    getBinding().nicknameInputLayout.error = "에러 테스트"
                    getBinding().nicknameEditText.setText(s.toString().replace(" ", ""))
                    getBinding().nicknameEditText.setSelection(getBinding().nicknameEditText.text.toString().length)
                }
            }
        })
    }

    private fun subscribeRemoveImageEvent() {
        editProfileViewModel.removeImageEvent.observe(viewLifecycleOwner, EventObserver {
            Glide.with(requireContext()).load(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_hero))
                    .into(getBinding().profileImage)
        })
    }

    private fun subscribePermissionEvent() {
        containerViewModel.grantedEvent.observe(viewLifecycleOwner, EventObserver {
            TedImagePicker.with(requireContext())
                    .min(1, "")
                    .max(5, "You can only select 5 images.")
                    .startMultiImage { uriList ->
                        uriList.map { uri ->
                            requireActivity().contentResolver.query(uri, null, null, null, null )?.use { cursor ->
                                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                                while(cursor.moveToNext()) {
                                    val id = cursor.getLong(idColumn)
                                    val size = cursor.getString( cursor.getColumnIndex( OpenableColumns.SIZE ))
                                    val sourceFileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ))

                                    val file = File(requireContext().filesDir, sourceFileName)

                                    if(sourceFileName.endsWith(".gif")) {
                                        if(BitmapUtils.getSizeToMega(size ?: "0") > 10) {
                                            Toast.makeText(requireContext(), "Image size is too big (less than 10MB)", Toast.LENGTH_SHORT).show()
                                        } else {
                                            requireActivity().contentResolver.openInputStream(uri).use { stream ->
                                                stream?.let { inputStream ->
                                                    BitmapUtils.copyStreamToFile(inputStream, file)
                                                    Glide.with(requireContext()).load(file)
                                                            .placeholder(R.drawable.ic_hero)
                                                            .into(getBinding().profileImage)
                                                }
                                            }
                                        }
                                    } else {
                                        requireActivity().contentResolver.openFileDescriptor(uri, "r").use { pfd ->
                                            val bitmap = BitmapFactory.decodeFileDescriptor(pfd?.fileDescriptor)
                                            Glide.with(requireContext()).load(BitmapUtils.resizeAndCompressImage(bitmap, file, size ?: "0"))
                                                    .placeholder(R.drawable.ic_hero)
                                                    .into(getBinding().profileImage)
                                        }
                                    }
                                }
                            }
                        }
                    }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.ok -> {
                //myPageViewModel.onClickSetting(getBinding().root)
                findNavController().navigateUp()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    
    override fun onResume() {
        super.onResume()
        navigationHost?.registerToolbarWithNavigation(getBinding().toolbar, getString(R.string.title_edit_profile))
    }
}