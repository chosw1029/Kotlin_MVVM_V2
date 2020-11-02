package com.nextus.kotlinmvvmexample.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentHomeBinding
import com.nextus.kotlinmvvmexample.ui.SnackbarMessage
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import com.nextus.kotlinmvvmexample.ui.main.MainFragmentDirections.Companion.toBoard
import com.nextus.kotlinmvvmexample.ui.message.SnackbarMessageManager
import com.nextus.kotlinmvvmexample.ui.setUpSnackbar
import com.nextus.kotlinmvvmexample.widget.FadingSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(homeViewModel)
        initView()
        initSnackbar()
    }

    private fun initView() {
        getBinding().btnSnackbar.setOnClickListener {
            snackbarMessageManager.addMessage(
                SnackbarMessage(
                    messageId = R.string.close_app,
                    actionId = R.string.ok,
                    requestChangeId = UUID.randomUUID().toString()
                )
            )
        }

        getBinding().btnBoard.setOnClickListener {
            findNavController().navigate(toBoard())
        }
    }

    private fun initSnackbar() {
        val snackbarLayout = requireActivity().findViewById<FadingSnackbar>(R.id.snackbar)

        setUpSnackbar(homeViewModel.snackBarMessage, snackbarLayout, snackbarMessageManager)
    }
}