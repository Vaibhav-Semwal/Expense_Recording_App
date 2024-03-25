package com.example.expenserecordingapp.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenserecordingapp.data.local.Item
import com.example.expenserecordingapp.data.local.ItemsRepository
import com.example.expenserecordingapp.ui.item.formatedExpense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HistoryViewModel(itemsRepository: ItemsRepository): ViewModel() {
    private val currentDate = Calendar.getInstance().get(Calendar.DATE)

    var historyUiState : StateFlow<HistoryUiState> = MutableStateFlow(HistoryUiState())

    init {
        viewModelScope.launch {
            historyUiState = itemsRepository.getAllItemsExceptToday(date = currentDate)
                .map {Log.d("Scope Completed","${it}")
                    HistoryUiState(itemList = it)
                }
                .stateIn(
                    scope = viewModelScope,
                    initialValue = HistoryUiState(),
                    started = SharingStarted.WhileSubscribed(1_000L)
                )
        }
    }
}

data class HistoryUiState(val itemList: List<Item> = listOf())