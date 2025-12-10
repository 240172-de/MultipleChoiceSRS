package com.example.multiplechoicesrs.model.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.datastore.DataStoreInstance
import com.example.multiplechoicesrs.model.ConnectionData
import com.example.multiplechoicesrs.model.DecksJson
import com.example.multiplechoicesrs.rest.MultipleChoiceApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ImportDecksUiState {
    data class Success(val decks: DecksJson, val connectionData: ConnectionData): ImportDecksUiState
    object Error: ImportDecksUiState
    object Loading: ImportDecksUiState
}

class ImportDecksViewModel(application: Application): AndroidViewModel(application) {
    var importDecksUiState: ImportDecksUiState by mutableStateOf(ImportDecksUiState.Loading)
        private set

    init {
        loadDecks()
    }

    fun loadDecks() {
        viewModelScope.launch {
            importDecksUiState = ImportDecksUiState.Loading

            DataStoreInstance.getConnectionPreferences(getApplication()).collect { data ->
                val connData = ConnectionData(
                    data.ipAddress,
                    data.port
                )

                getDecks(connData)
            }
        }
    }

    private fun getDecks(connectionData: ConnectionData) {
        viewModelScope.launch {
            importDecksUiState = try {
                val decks = MultipleChoiceApi(connectionData).retrofitService.getDecks()
                ImportDecksUiState.Success(
                    decks = decks,
                    connectionData = connectionData
                )
            } catch (_: IOException) {
                ImportDecksUiState.Error
            } catch (_: HttpException) {
                ImportDecksUiState.Error
            }
        }
    }
}