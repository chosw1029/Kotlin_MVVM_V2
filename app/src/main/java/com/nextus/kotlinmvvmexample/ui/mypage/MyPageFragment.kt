package com.nextus.kotlinmvvmexample.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentMypageBinding
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(myPageViewModel)
    }

}