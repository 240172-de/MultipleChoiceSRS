package com.example.multiplechoicesrs.model

/**
 * timestamp to ZonedDateTime via ZonedDateTime.parse()
 */
data class StudySession(
    val studySessionId: Int = -1,
    val timestamp: String,
    val numCorrectFirst: Int,
    val numIncorrectFirst: Int,
    val numCorrectTotal: Int,
    val numIncorrectTotal: Int
)
