package com.example.multiplechoicesrs.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionResult(
    val questionId: Int,
    var numCorrect: Int,
    var dateDue: String,
    var status: QuestionStatus,
    var box: Int
)
