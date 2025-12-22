package com.example.multiplechoicesrs.logic

import android.content.Context
import com.example.multiplechoicesrs.db.AnswerTableHelper
import com.example.multiplechoicesrs.db.QuestionResultTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.db.StudySessionTableHelper
import com.example.multiplechoicesrs.ext.getDueQuestions
import com.example.multiplechoicesrs.ext.hasDueQuestions
import com.example.multiplechoicesrs.model.Answer
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.model.QuestionStatus
import com.example.multiplechoicesrs.model.StudySession
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.pow

class StudyHelper(context: Context) {
    private val questionTableHelper = QuestionTableHelper(context)
    private val studySessionTableHelper = StudySessionTableHelper(context)
    private val questionResultTableHelper = QuestionResultTableHelper(context)
    private val answerTableHelper = AnswerTableHelper(context)

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

        val hasNew = allQuestions.any { it.result.status == QuestionStatus.NEW }

        val numReviewToAdd = if (hasNew) {
            numQuestions - selectedQuestions.size - 1
        } else {
            numQuestions - selectedQuestions.size
        }

        selectedQuestions.addAll(review.take(numReviewToAdd))

        if (hasNew) {
            val new = allQuestions.filter { question ->
                question.result.status == QuestionStatus.NEW
            }

            val numNewToAdd = numQuestions - selectedQuestions.size
            selectedQuestions.addAll(new.take(numNewToAdd))
        }

        return selectedQuestions
    }

    fun onFinishStudySession(answerList: List<Answer>): StudySession {
        var numCorrectFirst = 0
        var numIncorrectFirst = 0
        var numCorrectTotal = 0
        val timestamp = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Japan")).toString()

        answerList.groupBy { it.questionId }.forEach { questionId, answers ->
            val isCorrect = updateQuestionResult(questionId, answers, timestamp)

            if (isCorrect == 1 && answers.size == 1) {
                numCorrectFirst++
            } else {
                numIncorrectFirst++
            }

            numCorrectTotal += isCorrect
        }

        val studySession = StudySession(
            timestamp = timestamp,
            numCorrectFirst = numCorrectFirst,
            numIncorrectFirst = numIncorrectFirst,
            numCorrectTotal = numCorrectTotal,
            numIncorrectTotal = answerList.size - numCorrectTotal
        )

        if (answerList.isNotEmpty()) {
            studySessionTableHelper.saveStudySession(studySession)
        }

        return studySession
    }

    /**
     * Returns 1 if answered correctly, 0 otherwise
     */
    private fun updateQuestionResult(questionId: Int, answerList: List<Answer>, timestamp: String): Int {
        val questionResult = questionResultTableHelper.getQuestionResult(questionId)!!

        var isNewStatusReview = false
        var newBox = questionResult.box

        answerList.forEach { answer ->
            if (answer.isCorrect) {
                isNewStatusReview = true
            } else {
                newBox -= 2
            }

            val toSave = answer.copy(timestamp = timestamp)
            answerTableHelper.saveAnswer(toSave)
        }

        if (answerList.size == 1) {
            newBox++
        } else if (newBox < 0) {
            newBox = 0
        }

        val numCorrect = if (isNewStatusReview) 1 else 0

        questionResult.dateDue = getDateDue(newBox)
        questionResult.status = if (isNewStatusReview) QuestionStatus.REVIEW else QuestionStatus.RELEARN
        questionResult.box = newBox

        questionResultTableHelper.saveQuestionResult(questionResult)

        return numCorrect
    }

    private fun getDateDue(box: Int): String {
        val interval = when(box) {
            in 0..1 -> 3.0.pow(box)
            in 2..3 -> 7 * 2.0.pow(box - 2)
            in 4..6 -> 30 * 3.0.pow(box - 4)
            in 7..9 -> 365 * 2.0.pow(box - 7)
            else -> 365 * 10.0
        }.toLong()

        return ZonedDateTime.now()
            .withZoneSameInstant(ZoneId.of("Japan"))
            .plusDays(interval)
            .withHour(3)
            .withMinute(0)
            .withSecond(0)
            .toString()
    }
}