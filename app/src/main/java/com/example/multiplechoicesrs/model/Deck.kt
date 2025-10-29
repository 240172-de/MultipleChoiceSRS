package com.example.multiplechoicesrs.model

data class Deck(
    val deckId: Int,
    val name: String,
    val versionId: Int,
    var categories: List<Category>? = null,
    var questions: List<Question>? = null
)