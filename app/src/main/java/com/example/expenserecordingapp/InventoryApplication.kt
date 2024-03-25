package com.example.expenserecordingapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.expenserecordingapp.data.UserPreferencesRepository
import com.example.expenserecordingapp.data.local.AppContainer
import com.example.expenserecordingapp.data.local.AppDataContainer

private const val CURRENT_BUDGET_NAME = "current Budget"
private val Context.dataStore:   DataStore<Preferences> by preferencesDataStore(
    name = CURRENT_BUDGET_NAME
)

class ExpenseApplication : Application() {

    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}