package com.example.multiplechoicesrs.model

import java.time.LocalDateTime

data class QuestionResult(
    val questionId: Int,
    var numCorrect: Int,
    var dateDue: LocalDateTime,
    var status: QuestionStatus,
    var box: Int
)
