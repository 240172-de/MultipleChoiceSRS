package com.example.multiplechoicesrs.rest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.model.DecksJson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ImportDecksUiState {
    data class Success(val decks: DecksJson) : ImportDecksUiState
    object Error : ImportDecksUiState
    object Loading : ImportDecksUiState
}

class ImportDecksViewModel : ViewModel() {
    var importDecksUiState: ImportDecksUiState by mutableStateOf(ImportDecksUiState.Loading)
        private set

    init {
        getDecks()
    }

    fun getDecks() {
        viewModelScope.launch {
            importDecksUiState = ImportDecksUiState.Loading
            importDecksUiState = try {
                val decks = MultipleChoiceApi.retrofitService.getDecks()
                ImportDecksUiState.Success(
                    decks = decks
                )
            } catch (_: IOException) {
                ImportDecksUiState.Error
            } catch (_: HttpException) {
                ImportDecksUiState.Error
            }
        }
    }
}