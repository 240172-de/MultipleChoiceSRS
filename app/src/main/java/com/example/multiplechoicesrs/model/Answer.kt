package com.example.multiplechoicesrs.model

import java.time.LocalDateTime

data class Answer(
    val answerId: Int,
    val questionId: Int,
    val timestamp: LocalDateTime,
    val answerGiven: Int,
    val isCorrect: Boolean
)
