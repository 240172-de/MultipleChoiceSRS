package com.example.multiplechoicesrs.ext

import com.example.multiplechoicesrs.logic.AnalysisQuestionNumWrongComparator
import com.example.multiplechoicesrs.logic.AnalysisQuestionRatioCorrectComparator
import com.example.multiplechoicesrs.logic.AnalysisQuestionSourceComparator
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.model.QuestionStatus
import com.example.multiplechoicesrs.model.viewmodel.AnalysisQuestionData
import com.example.multiplechoicesrs.view.dialog.SortBy
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun List<Question>.hasDueQuestions(): Boolean {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
    val today = formatter.parse(formatter.format(time))

    return this.any { question ->
        question.result.status == QuestionStatus.NEW ||
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

fun List<AnalysisQuestionData>.sort(by: SortBy): List<AnalysisQuestionData> {
    val ascending = by.ascending
    val comparator = when(by) {
        SortBy.ALPHABETICALLY_ASC, SortBy.ALPHABETICALLY_DESC -> AnalysisQuestionSourceComparator
        SortBy.NUM_WRONG_ASC, SortBy.NUM_WRONG_DESC -> AnalysisQuestionNumWrongComparator
        SortBy.RATIO_CORRECT_ASC, SortBy.RATIO_CORRECT_DESC -> AnalysisQuestionRatioCorrectComparator
    }

    val sorted = this.sortedWith { o1, o2 ->
        comparator.compare(o1, o2)
    }

    return if (ascending) {
        sorted
    } else {
        sorted.reversed()
    }
}