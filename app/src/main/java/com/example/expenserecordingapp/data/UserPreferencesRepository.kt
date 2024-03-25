package com.example.expenserecordingapp.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
){
    private companion object {
        val CURRENT_BUDGET = doublePreferencesKey("current_budget")
        const val TAG = "UserPreferencesRepo"
    }

    val currentBudget: Flow<Double> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[CURRENT_BUDGET] ?: 0.0
        }

    suspend fun saveBudget(currentBudget : Double) {
        dataStore.edit {preferences ->
            preferences[CURRENT_BUDGET] = currentBudget
        }
    }
}