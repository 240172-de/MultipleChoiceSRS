package com.example.multiplechoicesrs.logic

import android.content.Context
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.ext.getDueQuestions
import com.example.multiplechoicesrs.ext.hasDueQuestions
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.model.QuestionStatus

class StudyHelper(context: Context) {
    val questionTableHelper = QuestionTableHelper(context)

    fun hasDueCards(deckId: Int, categoryIdList: List<Int>): Boolean {
        val allQuestions = questionTableHelper.getQuestions(deckId, categoryIdList)
        val hasRelearnOrNew = allQuestions.any { question ->
            question.result.status == QuestionStatus.RELEARN ||
                    question.result.status == QuestionStatus.NEW
        }

        return hasRelearnOrNew || allQuestions.hasDueQuestions()
    }

    fun getQuestions(deckId: Int, categoryIdList: List<Int>, numQuestions: Int): List<Question> {
        val allQuestions = questionTableHelper.getQuestions(deckId, categoryIdList)
        val selectedQuestions = ArrayList<Question>(numQuestions)

        val relearn = allQuestions.filter { question ->
            question.result.status == QuestionStatus.RELEARN
        }

        selectedQuestions.addAll(relearn.take(numQuestions))

        if (selectedQuestions.size == numQuestions) {
            return selectedQuestions
        }

        val review = if (allQuestions.hasDueQuestions()) {
            allQuestions.getDueQuestions()
        } else {
            allQuestions.filter { question ->
                question.result.status == QuestionStatus.REVIEW
            }
        }.sortedBy { question ->
            question.result.dateDue
        }

        val numReviewToAdd = numQuestions - selectedQuestions.size - 1
        selectedQuestions.addAll(review.take(numReviewToAdd))

        val new = allQuestions.filter { question ->
            question.result.status == QuestionStatus.NEW
        }

        val numNewToAdd = numQuestions - selectedQuestions.size
        selectedQuestions.addAll(new.take(numNewToAdd))

        return selectedQuestions
    }
}