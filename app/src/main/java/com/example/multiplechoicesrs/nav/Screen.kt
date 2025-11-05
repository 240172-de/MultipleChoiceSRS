package com.example.multiplechoicesrs.nav

import com.example.multiplechoicesrs.model.Deck
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object ImportDataScreen

    @Serializable
    data object DeckListScreen

    @Serializable
    data class CategoryListScreen(
        val deck: Deck
    )

    @Serializable
    data class StudyScreen(
        val deck: Deck,
        val categoryIdList: List<Int>,
        val numToStudy: Int
    )
}