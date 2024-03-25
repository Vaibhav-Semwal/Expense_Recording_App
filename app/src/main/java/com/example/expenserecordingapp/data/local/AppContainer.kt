package com.example.expenserecordingapp.data.local

import android.content.Context


interface AppContainer {
    val itemsRepository: ItemsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(ExpenseDatabase.getDatabase(context).itemDao())
    }
}
