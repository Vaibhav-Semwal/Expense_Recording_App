package com.example.expenserecordingapp.ui.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenserecordingapp.data.UserPreferencesRepository
import com.example.expenserecordingapp.data.local.Item
import com.example.expenserecordingapp.data.local.ItemsRepository
import com.example.expenserecordingapp.ui.theme.primary_expense_container
import com.example.expenserecordingapp.ui.theme.primary_expense_container_alternate
import com.example.expenserecordingapp.ui.theme.primary_expense_container_alternate_dark
import com.example.expenserecordingapp.ui.theme.primary_expense_container_dark
import com.example.expenserecordingapp.ui.theme.secondary_expense_container
import com.example.expenserecordingapp.ui.theme.secondary_expense_container_alternate
import com.example.expenserecordingapp.ui.theme.secondary_expense_container_alternate_dark
import com.example.expenserecordingapp.ui.theme.secondary_expense_container_dark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    itemsRepository: ItemsRepository
): ViewModel() {
    private val currentDate = Calendar.getInstance().get(Calendar.DATE)

    var currentBudget : StateFlow<Double> = MutableStateFlow(0.0)
    var homeUiState : StateFlow<HomeUiState> = MutableStateFlow(HomeUiState())

    init {
        viewModelScope.launch {
            currentBudget = userPreferencesRepository.currentBudget
                .stateIn(
                    scope = viewModelScope
                )
            homeUiState = itemsRepository.getAllTodayItems(date = currentDate)
                .map { HomeUiState(itemList = it) }
                .stateIn(
                    scope = viewModelScope,
                    initialValue = HomeUiState(),
                    started = SharingStarted.WhileSubscribed(1_000L)
                )
        }
    }

    suspend fun getBudget(): Double{
        return userPreferencesRepository.currentBudget.first()
    }

    fun setBudget(budget: Double){
        viewModelScope.launch {
            userPreferencesRepository.saveBudget(budget)
        }
    }
}

fun getPrimaryColor(darkTheme: Boolean): List<Color>{
    return if (darkTheme){
        listOf(primary_expense_container_dark, primary_expense_container_alternate_dark)
    }else{
        listOf(primary_expense_container, primary_expense_container_alternate)
    }
}

fun getSecondaryColor(darkTheme: Boolean): List<Color>{
    return if (darkTheme){
        listOf(secondary_expense_container_dark, secondary_expense_container_alternate_dark)
    }else{
        listOf(secondary_expense_container, secondary_expense_container_alternate)
    }
}

data class HomeUiState(val itemList: List<Item> = listOf())