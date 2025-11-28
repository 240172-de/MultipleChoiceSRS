package com.example.multiplechoicesrs.model.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.db.DeckTableHelper
import com.example.multiplechoicesrs.model.Deck
import kotlinx.coroutines.launch

sealed interface DeleteDeckUiState {
    data class Success(val decks: List<Deck>): DeleteDeckUiState
    object NoData: DeleteDeckUiState
    object Loading: DeleteDeckUiState
}

class DeleteDeckViewModel(context: Context): ViewModel() {
    private val deckTableHelper = DeckTableHelper(context)
    var deleteDeckUiState: DeleteDeckUiState by mutableStateOf(DeleteDeckUiState.Loading)
        private set

    init {
        getDecks()
    }

    fun getDecks() {
        viewModelScope.launch {
            deleteDeckUiState = DeleteDeckUiState.Loading
            val decks = deckTableHelper.getDecks()

            deleteDeckUiState = if (decks.isEmpty()) {
                DeleteDeckUiState.NoData
            } else {
                DeleteDeckUiState.Success(decks)
            }
        }
    }
}