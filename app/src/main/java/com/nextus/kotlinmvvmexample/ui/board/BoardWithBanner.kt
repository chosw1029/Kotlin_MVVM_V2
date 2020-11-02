package com.nextus.kotlinmvvmexample.ui.board

import com.google.android.gms.ads.AdView
import com.nextus.kotlinmvvm.model.Board

data class BoardWithBanner(
    val board: Board,
    val adView: AdView?
)