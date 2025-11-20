package com.example.multiplechoicesrs.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionJson(
    @SerialName("deck_id")
    val deckId: Int,

    @SerialName("category_id")
    val categoryId: Int,

    @SerialName("question_id")
    val questionId: Int,
    val question: String,

    @SerialName("question_image")
    val questionImage: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,

    @SerialName("answer1_image")
    val answer1Image: String,

    @SerialName("answer2_image")
    val answer2Image: String,

    @SerialName("answer3_image")
    val answer3Image: String,

    @SerialName("answer4_image")
    val answer4Image: String,

    @SerialName("correct_answer")
    val correctAnswer: Int,
    val explanation: String,
    val source: String
)