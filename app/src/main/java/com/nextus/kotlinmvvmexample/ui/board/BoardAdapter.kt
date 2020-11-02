package com.nextus.kotlinmvvmexample.ui.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.ItemBannerBinding
import com.nextus.kotlinmvvmexample.databinding.ItemBoardBinding
import com.nextus.kotlinmvvmexample.ui.board.BoardFragmentDirections.Companion.toBoardDetail
import com.nextus.kotlinmvvmexample.util.executeAfter

class BoardAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val itemsPerAd: Int
): ListAdapter<BoardWithBanner, BoardViewHolder>(BoardDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_board -> BoardViewHolder.BoardItemViewHolder(
                    ItemBoardBinding.inflate(inflater, parent, false)
            )

            R.layout.item_banner -> BoardViewHolder.BannerViewHolder(
                    ItemBannerBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        when(holder) {
            is BoardViewHolder.BoardItemViewHolder -> holder.binding.executeAfter {
                lifecycleOwner = this@BoardAdapter.lifecycleOwner
                item = getItem(position).board
                root.setOnClickListener {
                    it.findNavController().navigate(toBoardDetail())
                }
            }
            is BoardViewHolder.BannerViewHolder -> holder.binding.executeAfter {
                val adCardView = holder.itemView as ViewGroup

                if(adCardView.childCount > 0)
                    adCardView.removeAllViews()

                getItem(position).adView?.let {
                    if(it.parent != null) {
                        (it.parent as ViewGroup).removeView(it)
                    }

                    adCardView.addView(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 0 && position % itemsPerAd == 0) R.layout.item_banner else R.layout.item_board
    }
}

object BoardDiff : DiffUtil.ItemCallback<BoardWithBanner>() {
    override fun areItemsTheSame(oldItem: BoardWithBanner, newItem: BoardWithBanner): Boolean {
        return oldItem.board.title == newItem.board.title
    }

    override fun areContentsTheSame(oldItem: BoardWithBanner, newItem: BoardWithBanner) = oldItem == newItem
}

/**
 * [RecyclerView.ViewHolder] types used by this adapter.
 */
sealed class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class BoardItemViewHolder(val binding: ItemBoardBinding) : BoardViewHolder(binding.root)

    class BannerViewHolder(val binding: ItemBannerBinding) : BoardViewHolder(binding.root)

}
