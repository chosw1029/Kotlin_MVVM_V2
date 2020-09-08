package com.nextus.kotlinmvvmexample.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentHomeBinding
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(homeViewModel)
    }
}