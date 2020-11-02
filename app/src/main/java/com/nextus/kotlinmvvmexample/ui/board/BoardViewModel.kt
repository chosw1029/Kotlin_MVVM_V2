package com.nextus.kotlinmvvmexample.ui.board

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nextus.kotlinmvvm.model.Board
import com.nextus.kotlinmvvmexample.shared.result.Event

class BoardViewModel: ViewModel() {

    val boardItemList = ObservableArrayList<BoardWithBanner>()
    val boardItemEvent = MutableLiveData<Event<Unit>>()

    var page = 0

    init {

    }

    fun addBoardItem() {
        val beforeSize = if(boardItemList.size > 0) boardItemList.size - 1 else boardItemList.size
        for(i in beforeSize .. beforeSize + 30) {
            boardItemList.add(BoardWithBanner(Board("Item ${i+1}"), null))
        }

        page++
        boardItemEvent.value = Event(Unit)
    }
}