package com.example.multiplechoicesrs.model

data class QuestionJson(
    val deckId: Int,
    val categoryId: Int,
    val questionId: Int,
    val question: String,
    val questionImage: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val answer1Image: String,
    val answer2Image: String,
    val answer3Image: String,
    val answer4Image: String,
    val correctAnswer: Int,
    val explanation: String,
    val source: String
)