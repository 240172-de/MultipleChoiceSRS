package com.example.multiplechoicesrs.model

import android.media.Image

data class Question(
    val deckId: Int,
    val categoryId: Int,
    val questionId: Int,
    val question: String,
    val questionImage: Image,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val answer1Image: Image,
    val answer2Image: Image,
    val answer3Image: Image,
    val answer4Image: Image,
    val correctAnswer: Int,
    val explanation: String,
    val source: String
)
