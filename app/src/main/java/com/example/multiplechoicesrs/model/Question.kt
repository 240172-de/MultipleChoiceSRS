package com.example.multiplechoicesrs.model

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val deckId: Int,
    val categoryId: Int,
    val questionId: Int,
    val question: String,
    val questionImage: ImageBitmap?,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val answer1Image: ImageBitmap?,
    val answer2Image: ImageBitmap?,
    val answer3Image: ImageBitmap?,
    val answer4Image: ImageBitmap?,
    val correctAnswer: Int,
    val explanation: String,
    val source: String,
    val result: QuestionResult
)
