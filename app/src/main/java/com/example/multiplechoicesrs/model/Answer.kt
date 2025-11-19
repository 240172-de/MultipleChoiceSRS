package com.example.multiplechoicesrs.model

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * timestamp to ZonedDateTime via ZonedDateTime.parse()
 */
data class Answer(
    val answerId: Int = -1,
    val questionId: Int,
    val timestamp: String = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Japan")).toString(),
    val answerGiven: Int,
    val isCorrect: Boolean
)
