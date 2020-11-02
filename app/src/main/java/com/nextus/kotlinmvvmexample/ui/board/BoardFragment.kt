package com.nextus.kotlinmvvmexample.ui.board

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.nextus.kotlinmvvm.model.Board
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentBoardBinding
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BoardFragment: BaseFragment<FragmentBoardBinding>(R.layout.fragment_board) {

    companion object {
        private const val ITEMS_PER_AD = 29
    }

    private val boardViewModel: BoardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(boardViewModel)
        initBoardRecycler()
        initScrollListener()

        subscribeBoardItemEvent()
        boardViewModel.addBoardItem()
        //initBannerAds()
    }

    private fun initBoardRecycler() {
        getBinding().boardRecycler.adapter = BoardAdapter(viewLifecycleOwner, ITEMS_PER_AD)
        getBinding().boardRecycler.itemAnimator = DefaultItemAnimator()
        getBinding().boardRecycler.setHasFixedSize(true)
    }

    /**
     * 페이징 로딩
     */
    private fun initScrollListener() {
        getBinding().boardRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (layoutManager != null &&
                        layoutManager.findLastCompletelyVisibleItemPosition() == boardViewModel.boardItemList.size - 1) { //bottom of list!
                    boardViewModel.addBoardItem()
                }
            }
        })
    }

    private fun subscribeBoardItemEvent() {
        boardViewModel.boardItemEvent.observe(viewLifecycleOwner, Observer {
            initBannerAds()
            (getBinding().boardRecycler.adapter as BoardAdapter).submitList(boardViewModel.boardItemList.toMutableList())
        })
    }

    private fun initBannerAds() {
        // Loop through the items array and place a new banner ad in every ith position in
        // the items List.
        val adView = AdView(requireContext())
        adView.adSize = AdSize.SMART_BANNER
        adView.adUnitId = "ca-app-pub-3940256099942544/4177191030"
        boardViewModel.boardItemList.add(boardViewModel.page * ITEMS_PER_AD, BoardWithBanner(Board("Item Banner"), adView))
        loadBannerAd(ITEMS_PER_AD)
    }

    /**
     * Loads the banner ads in the items list.
     */
    private fun loadBannerAd(index: Int) {
        if (index >= boardViewModel.boardItemList.size) {
            return
        }

        boardViewModel.boardItemList[index].adView?.let {
            // Set an AdListener on the AdView to wait for the previous banner ad
            // to finish loading before loading the next ad in the items list.
            it.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    // The previous banner ad loaded successfully, call this method again to
                    // load the next ad in the items list.
                    loadBannerAd(index + ITEMS_PER_AD)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // The previous banner ad failed to load. Call this method again to load
                    // the next ad in the items list.
                    val error = String.format(
                            "domain: %s, code: %d, message: %s",
                            loadAdError.domain, loadAdError.code, loadAdError.message)
                    Timber.e(error)
                    loadBannerAd(index + ITEMS_PER_AD)
                }
            }

            // Load the banner ad.
            it.loadAd(AdRequest.Builder().build())
        }
    }

    override fun onResume() {
        for (item in boardViewModel.boardItemList) {
            item.adView?.resume()
        }
        super.onResume()
    }

    override fun onPause() {
        for (item in boardViewModel.boardItemList) {
            item.adView?.pause()
        }
        super.onPause()
    }

    override fun onDestroy() {
        for (item in boardViewModel.boardItemList) {
            item.adView?.destroy()
        }
        super.onDestroy()
    }
}