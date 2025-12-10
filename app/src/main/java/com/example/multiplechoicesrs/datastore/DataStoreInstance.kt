package com.example.multiplechoicesrs.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.multiplechoicesrs.model.ConnectionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreInstance {
    private const val USER_PREFERENCES_NAME = "user_preferences"

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    object PreferencesKeys {
        val NUM_TO_STUDY = intPreferencesKey("num_to_study")
        val IP_ADDRESS = stringPreferencesKey("ip_address")
        val PORT = stringPreferencesKey("port")
    }

    suspend fun <T> savePreferences(context: Context, key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> getPreferences(context: Context, key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    suspend fun saveConnectionPreferences(context: Context, data: ConnectionData) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IP_ADDRESS] = data.ipAddress
            preferences[PreferencesKeys.PORT] = data.port
        }
    }

    fun getConnectionPreferences(context: Context): Flow<ConnectionData> {
        return context.dataStore.data.map { preferences ->
            ConnectionData(
                preferences[PreferencesKeys.IP_ADDRESS] ?: "192.168.91.31",
                preferences[PreferencesKeys.PORT] ?: "80"
            )
        }
    }
}