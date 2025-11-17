package com.example.multiplechoicesrs.ext

import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.model.QuestionStatus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun List<Question>.hasDueQuestions(): Boolean {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
    val today = formatter.parse(formatter.format(time))

    return this.any { question ->
        question.result.status == QuestionStatus.REVIEW &&
                today!! >= formatter.parse(question.result.dateDue)
    }
}

fun List<Question>.getDueQuestions(): List<Question> {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
    val today = formatter.parse(formatter.format(time))

    return this.filter { question ->
        question.result.status == QuestionStatus.REVIEW &&
                today!! >= formatter.parse(question.result.dateDue)
    }
}