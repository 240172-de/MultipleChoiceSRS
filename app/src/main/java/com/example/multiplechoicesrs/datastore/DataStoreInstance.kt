package com.example.multiplechoicesrs.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreInstance {
    private const val USER_PREFERENCES_NAME = "user_preferences"

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    object PreferencesKeys {
        val NUM_TO_STUDY = intPreferencesKey("num_to_study")
    }

    suspend fun saveIntPreferences(context: Context, key: Preferences.Key<Int>, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getIntPreferences(context: Context, key: Preferences.Key<Int>): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[key]
        }
    }
}