package com.example.multiplechoicesrs.logic

import android.content.Context
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.Question

class StudyHelper(context: Context) {
    val questionTableHelper = QuestionTableHelper(context)

    fun hasDueCards(categoryIdList: List<Int>): Boolean {
        //TODO: Check if exists
        //Status New/Relearn
        //Status Review with dateDue <= today
        return false
    }

    fun getQuestions(deckId: Int, categoryIdList: List<Int>, numQuestions: Int): List<Question> {
        val allQuestions = questionTableHelper.getQuestions(deckId, categoryIdList)
        val selectedQuestions = ArrayList<Question>(numQuestions)
        //TODO: Get QuestionResults
        //First Prio Relearn
        //Then Review + min 1 New (if exists)
        //Study ahead if list still empty after Review added
        selectedQuestions.addAll(allQuestions)

        return selectedQuestions
    }
}