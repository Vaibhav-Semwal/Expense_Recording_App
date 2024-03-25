package com.example.expenserecordingapp.ui

import android.app.Application
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.expenserecordingapp.ExpenseApplication
import com.example.expenserecordingapp.ui.history.HistoryViewModel
import com.example.expenserecordingapp.ui.home.HomeScreenViewModel
import com.example.expenserecordingapp.ui.item.ItemDetailsViewModel
import com.example.expenserecordingapp.ui.item.ItemEditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel(
                inventoryApplication().userPreferencesRepository,
                inventoryApplication().container.itemsRepository
            )
        }
        initializer {
            ItemDetailsViewModel(inventoryApplication().container.itemsRepository)
        }
        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.itemsRepository
            )
        }
        initializer {
            HistoryViewModel(inventoryApplication().container.itemsRepository)
        }
    }
}

fun CreationExtras.inventoryApplication(): ExpenseApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExpenseApplication)