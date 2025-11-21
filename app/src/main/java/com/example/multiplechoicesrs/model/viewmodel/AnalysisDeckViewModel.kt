package com.example.multiplechoicesrs.model.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.db.AnswerTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.Answer
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.PieChartDataEntry
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import kotlinx.coroutines.launch

sealed interface AnalysisDeckUiState {
    data class Success(val pieChartData: PieChartData): AnalysisDeckUiState
    object Loading: AnalysisDeckUiState
    object NoData: AnalysisDeckUiState
}

class AnalysisDeckViewModel(
    context: Context,
    private val deckId: Int
): ViewModel() {
    val questionTableHelper = QuestionTableHelper(context)
    val answerTableHelper = AnswerTableHelper(context)
    var analysisDeckUiState: AnalysisDeckUiState by mutableStateOf(AnalysisDeckUiState.Loading)
        private set

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            analysisDeckUiState = AnalysisDeckUiState.Loading

            val questionIds = questionTableHelper.getQuestions(deckId).map { it.questionId }
            val answers = mutableListOf<Answer>()

            questionIds.forEach { questionId ->
                answers.addAll(answerTableHelper.getAnswers(questionId))
            }

            if (answers.isEmpty()) {
                analysisDeckUiState = AnalysisDeckUiState.NoData
            } else {
                val numTotal = answers.size
                val numCorrect = answers.count { it.isCorrect }
                val numIncorrect = numTotal - numCorrect

                val percentageCorrect = numCorrect.toFloat() / numTotal
                val percentageIncorrect = numIncorrect.toFloat() / numTotal

                analysisDeckUiState = AnalysisDeckUiState.Success(
                    PieChartData(
                        "全体",
                        listOf(
                            PieChartDataEntry("正解", percentageCorrect, GreenCorrectAnswer),
                            PieChartDataEntry("不正解", percentageIncorrect, RedIncorrectAnswer)
                        )
                    )
                )
            }
        }
    }
}