package com.example.multiplechoicesrs.model

data class StudySession(
    val studySessionId: Int,
    val timestamp: String,
    val numCorrect: Int,
    val numIncorrect: Int
)
