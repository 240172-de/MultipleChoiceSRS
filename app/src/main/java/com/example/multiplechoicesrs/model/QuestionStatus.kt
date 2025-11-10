package com.example.multiplechoicesrs.model

enum class QuestionStatus {
    NEW,
    REVIEW,
    RELEARN,
    RETIRED;

    companion object {
        fun get(ordinal: Int): QuestionStatus {
            return entries[ordinal]
        }
    }
}