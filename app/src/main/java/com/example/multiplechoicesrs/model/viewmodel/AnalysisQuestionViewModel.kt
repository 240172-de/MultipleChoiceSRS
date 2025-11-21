package com.example.multiplechoicesrs.model.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multiplechoicesrs.db.AnswerTableHelper
import com.example.multiplechoicesrs.db.QuestionTableHelper
import com.example.multiplechoicesrs.model.PieChartData
import com.example.multiplechoicesrs.model.PieChartDataEntry
import com.example.multiplechoicesrs.model.Question
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import kotlinx.coroutines.launch

data class AnalysisQuestionData(
    val question: Question,
    val pieChartData: PieChartData
)

sealed interface AnalysisQuestionUiState {
    data class Success(val data: List<AnalysisQuestionData>): AnalysisQuestionUiState
    object Loading: AnalysisQuestionUiState
    object NoData: AnalysisQuestionUiState
}

class AnalysisQuestionViewModel(
    context: Context,
    private val deckId: Int
): ViewModel() {
    val questionTableHelper = QuestionTableHelper(context)
    val answerTableHelper = AnswerTableHelper(context)
    var analysisQuestionUiState: AnalysisQuestionUiState by mutableStateOf(AnalysisQuestionUiState.Loading)
        private set

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            analysisQuestionUiState = AnalysisQuestionUiState.Loading

            val data = mutableListOf<AnalysisQuestionData>()
            val questionList = questionTableHelper.getQuestions(deckId)

            questionList.forEach { question ->
                val answers = answerTableHelper.getAnswers(question.questionId)

                if (answers.isNotEmpty()) {
                    val numTotal = answers.size
                    val numCorrect = answers.count { it.isCorrect }
                    val numIncorrect = numTotal - numCorrect

                    val percentageCorrect = numCorrect.toFloat() / numTotal
                    val percentageIncorrect = numIncorrect.toFloat() / numTotal

                    data.add(AnalysisQuestionData(
                        question,
                        PieChartData(
                            "正解率",
                            listOf(
                                PieChartDataEntry("正解", percentageCorrect, GreenCorrectAnswer),
                                PieChartDataEntry("不正解", percentageIncorrect, RedIncorrectAnswer)
                            )
                        )
                    ))
                }
            }

            analysisQuestionUiState = if (data.isEmpty()) {
                AnalysisQuestionUiState.NoData
            } else {
                AnalysisQuestionUiState.Success(data)
            }
        }
    }
}