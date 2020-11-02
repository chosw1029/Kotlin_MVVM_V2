package com.nextus.kotlinmvvmexample.ui.board.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentBoardDetailBinding
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardDetailFragment: BaseFragment<FragmentBoardDetailBinding>(R.layout.fragment_board_detail) {

    private val boardDetailViewModel: BoardDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(boardDetailViewModel)
    }
}