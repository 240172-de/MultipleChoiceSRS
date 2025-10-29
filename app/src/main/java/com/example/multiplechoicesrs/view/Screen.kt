package com.example.multiplechoicesrs.view

import kotlinx.serialization.Serializable
sealed class Screen {
    @Serializable
    data object ImportDataScreen

    @Serializable
    data object DeckListScreen

    @Serializable
    data class CategoryListScreen(
        val deckId: Int
    )

    @Serializable
    data class StudyScreen(
        val deckId: Int,
        val categoryIdList: List<Int>
    )
}