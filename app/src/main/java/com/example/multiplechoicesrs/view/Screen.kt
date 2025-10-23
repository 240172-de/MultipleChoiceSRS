package com.example.multiplechoicesrs.view

import kotlinx.serialization.Serializable
sealed class Screen {
    @Serializable
    data object ImportDataScreen

    @Serializable
    data object DeckListScreen
}