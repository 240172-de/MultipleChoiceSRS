package com.example.multiplechoicesrs.model

data class Category(
    val deckId: Int,
    val categoryId: Int,
    val name: String
) {
    fun getDueQuestions(): List<Question> {
        //TODO:
        return emptyList()
    }

    fun getAllQuestions(): List<Question> {
        return emptyList()
    }
}
