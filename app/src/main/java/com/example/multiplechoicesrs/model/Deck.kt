package com.example.multiplechoicesrs.model

data class Deck(
    val deckId: Int,
    val name: String
) {
    fun getCategories(): List<Category> {
        //TODO:
        return emptyList()
    }

    fun getDueQuestions(): List<Question> {
        return emptyList()
    }

    fun getAllQuestions(): List<Question> {
        return emptyList()
    }
}