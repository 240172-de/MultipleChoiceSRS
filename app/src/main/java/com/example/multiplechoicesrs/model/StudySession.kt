package com.example.multiplechoicesrs.model

import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * timestamp to ZonedDateTime via ZonedDateTime.parse()
 */
data class StudySession(
    val studySessionId: Int = -1,
    val timestamp: String = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Japan")).toString(),
    val numCorrect: Int,
    val numIncorrect: Int
)
