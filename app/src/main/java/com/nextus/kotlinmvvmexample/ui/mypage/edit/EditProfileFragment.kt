package com.nextus.kotlinmvvmexample.ui.mypage.edit

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentEditProfileBinding
import com.nextus.kotlinmvvmexample.shared.result.EventObserver
import com.nextus.kotlinmvvmexample.ui.ContainerViewModel
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

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
            if(it.contentEquals("EditProfile")) {
                ImagePicker.with(this)
                        .galleryOnly()
                        .compress(1024 * 4)  // Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1920, 1080)  // Final image resolution will be less than 1080 x 1080(Optional)
                        .start { resultCode, data ->
                            when (resultCode) {
                                Activity.RESULT_OK -> {
                                    //You can get File object from intent
                                    val file = ImagePicker.getFile(data)

                                    //getBinding().profileImage.setImageBitmap(BitmapFactory.decodeFile(file?.path))

                                    Glide.with(requireContext()).load(file)
                                            .placeholder(R.drawable.ic_hero)
                                            .into(getBinding().profileImage)
                                    //You can also get File Path from intent
                                    val filePath = ImagePicker.getFilePath(data)

                                    editProfileViewModel.removeIconVisibility.value = true
                                }
                                ImagePicker.RESULT_ERROR -> {
                                    //Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    //Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
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